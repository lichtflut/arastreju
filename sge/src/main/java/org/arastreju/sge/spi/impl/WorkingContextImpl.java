/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.spi.impl;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.index.ConversationIndex;
import org.arastreju.sge.inferencing.implicit.InverseOfInferencer;
import org.arastreju.sge.inferencing.implicit.SubClassOfInferencer;
import org.arastreju.sge.inferencing.implicit.TypeInferencer;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.ResourceResolver;
import org.arastreju.sge.persistence.TxAction;
import org.arastreju.sge.persistence.TxResultAction;
import org.arastreju.sge.spi.AssociationResolver;
import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.WorkingContext;
import org.arastreju.sge.spi.tx.BoundTransactionControl;
import org.arastreju.sge.spi.tx.TxProvider;
import org.arastreju.sge.spi.uow.AssociationManager;
import org.arastreju.sge.spi.uow.IndexUpdateUOW;
import org.arastreju.sge.spi.uow.InferencingInterceptor;
import org.arastreju.sge.spi.uow.OpenConversationNotifier;
import org.arastreju.sge.spi.uow.ResourceResolverImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Handler for resolving, adding and removing of a node's association.
 * </p>
 *
 * <p>
 * 	Created Dec 1, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class WorkingContextImpl implements WorkingContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkingContextImpl.class);

	// ----------------------------------------------------

    private final ConversationContextImpl conversationContext = new ConversationContextImpl();

    private final GraphDataConnection connection;

    private final AssociationResolver associationResolver;

    private final TxProvider txProvider;

    private final ConversationIndex conversationIndex;

    private AssociationManager associationManager;

	// ----------------------------------------------------

	/**
	 * Creates a new Working Context.
	 */
	public WorkingContextImpl(GraphDataConnection connection) {
		LOGGER.debug("New conversation context {} started.", conversationContext.getID());
        this.connection = connection;
        this.associationResolver = connection.createAssociationResolver(this);
        this.txProvider = connection.createTxProvider(this);

        ResourceResolverImpl resolver = new ResourceResolverImpl(this);
        this.conversationIndex = new ConversationIndex(conversationContext, connection.getIndexProvider())
                .add(new TypeInferencer(resolver))
                .add(new SubClassOfInferencer(resolver));

        connection.register(this);
	}

	// ----------------------------------------------------

    @Override
    public AttachedAssociationKeeper lookup(QualifiedName qn) {
        AttachedAssociationKeeper registered = conversationContext.get(qn);
        if (registered != null && !registered.isAttached()) {
            LOGGER.warn("There is a detached AssociationKeeper in the conversation register: {}.", qn);
        }
        return registered;
    }

    @Override
    public AttachedAssociationKeeper find(QualifiedName qn) {
        AttachedAssociationKeeper registered = lookup(qn);
        if (registered != null) {
            return registered;
        }
        AttachedAssociationKeeper existing = connection.find(qn);
        if (existing != null) {
            attach(qn, existing);
            return existing;
        } else {
            return null;
        }
    }

    @Override
    public AttachedAssociationKeeper create(final QualifiedName qn) {
        conversationContext.assertActive();
        AttachedAssociationKeeper keeper = getTxProvider().doTransacted(new TxResultAction<AttachedAssociationKeeper>() {
            @Override
            public AttachedAssociationKeeper execute() {
                return connection.create(qn);
            }
        }, this);
        attach(qn, keeper);
        return keeper;
    }

    @Override
    public void remove(final QualifiedName qn) {
        conversationContext.assertActive();
        detach(qn);
        getTxProvider().doTransacted(new TxAction() {
            @Override
            public void execute() {
                connection.remove(qn);
                conversationIndex.remove(qn);
            }
        }, this);

    }

    // ----------------------------------------------------

    @Override
    public void attach(QualifiedName qn, AttachedAssociationKeeper keeper) {
        conversationContext.put(qn, keeper);
        keeper.setConversationContext(this);
    }

    @Override
    public void detach(QualifiedName qn) {
        final AttachedAssociationKeeper removed = conversationContext.remove(qn);
        if (removed != null) {
            removed.detach();
        }
    }

    // ----------------------------------------------------

    /**
     * Resolve the associations of given association keeper.
     * @param keeper The association keeper to be resolved.
     */
    @Override
    public void resolveAssociations(AttachedAssociationKeeper keeper) {
        conversationContext.assertActive();
        associationResolver.resolveAssociations(keeper);
    }

    @Override
    public void addAssociation(final AttachedAssociationKeeper keeper, final Statement stmt) {
        conversationContext.assertActive();
        getTxProvider().doTransacted(new TxAction() {
            @Override
            public void execute() {
                associationManager.addAssociation(keeper, stmt);
            }
        }, this);

    }

    @Override
    public boolean removeAssociation(final AttachedAssociationKeeper keeper, final Statement stmt) {
        conversationContext.assertActive();
        getTxProvider().doTransacted(new TxAction() {
            @Override
            public void execute() {
                associationManager.removeAssociation(keeper, stmt);
            }
        }, this);
        return true;
    }

    // ----------------------------------------------------

    @Override
    public void onModification(QualifiedName qualifiedName, WorkingContext otherContext) {
        AttachedAssociationKeeper existing = lookup(qualifiedName);
        if (existing != null) {
            LOGGER.info("Concurrent change on node {} in other context {}.", qualifiedName, otherContext);
            existing.notifyChanged();
        }
    }

    @Override
    public void beginUnitOfWork(BoundTransactionControl tx) {
        LOGGER.info("Starting new Unit of Work in conversation {}.", this);
        this.associationManager = createAssociationManager(tx);
    }

    // ----------------------------------------------------

	/**
	 * Close and invalidate this context.
	 */
    @Override
	public void close() {
		if (conversationContext.isActive()) {
            conversationContext.close();
            connection.unregister(this);
		}
	}

    @Override
    public boolean isActive() {
		return conversationContext.isActive();
	}

    // ----------------------------------------------------

    @Override
    public TxProvider getTxProvider() {
        return txProvider;
    }

    @Override
    public ConversationIndex getIndex() {
        return conversationIndex;
    }

    @Override
    public ConversationContext getConversationContext() {
        return conversationContext;
    }

    // ----------------------------------------------------

    @Override
    public String toString() {
        return "WorkingContext[" +  conversationContext.toString() + "]";
    }

    // ----------------------------------------------------

	@Override
	protected void finalize() throws Throwable {
        super.finalize();
		if (conversationContext.isActive()) {
			LOGGER.warn("Conversation context will be removed by GC, but has not been closed: {}"
                    , conversationContext.getID());
		}
	}

    // ----------------------------------------------------

    private AssociationManager createAssociationManager(BoundTransactionControl tx) {
        ResourceResolver resolver = new ResourceResolverImpl(this);
        IndexUpdateUOW indexUpdateUOW = new IndexUpdateUOW(conversationIndex);
        tx.register(indexUpdateUOW);

        AssociationManager am = new AssociationManager(resolver, tx);
        am.register(connection.createAssociationWriter(this));
        am.register(indexUpdateUOW);
        am.register(new InferencingInterceptor(am).add(new InverseOfInferencer(resolver)));
        am.register(new OpenConversationNotifier(connection, this));
        return am;
    }

}
