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

import org.arastreju.sge.Conversation;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.eh.meta.NotYetImplementedException;
import org.arastreju.sge.index.LuceneQueryBuilder;
import org.arastreju.sge.index.QNResolver;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.persistence.TxAction;
import org.arastreju.sge.persistence.TxResultAction;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.AssocKeeperAccess;
import org.arastreju.sge.spi.AttachedResourceNode;
import org.arastreju.sge.spi.ConversationController;
import org.arastreju.sge.spi.tx.TxProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * <p>
 *  Abstract base for modeling conversations.
 * </p>
 *
 * <p>
 *  Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class ConversationImpl implements Conversation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationImpl.class);

	private final ConversationController controller;

	// ----------------------------------------------------

    /**
     * Constructor.
     * @param controller The controller of this conversation.
     */
	public ConversationImpl(ConversationController controller) {
		this.controller = controller;
	}

    // -- Node operations ---------------------------------

    @Override
    public ResourceNode findResource(final QualifiedName qn) {
        AssociationKeeper existing = controller.find(qn);
        if (existing != null) {
            return new AttachedResourceNode(qn, existing);
        }
        return null;
    }

    @Override
    public ResourceNode resolve(final ResourceID resourceID) {
        AttachedAssociationKeeper existing = controller.find(resourceID.getQualifiedName());
        if (existing != null) {
            return new AttachedResourceNode(resourceID.getQualifiedName(), existing);
        } else {
            AssociationKeeper created = controller.create(resourceID.getQualifiedName());
            return new AttachedResourceNode(resourceID.getQualifiedName(), created);
        }
    }

    @Override
    public void attach(final ResourceNode resource) {
        assertActive();
        // 1st: check if node is already attached.
        if (resource.isAttached()){
            verifySameContext(resource);
            return;
        }
        tx().doTransacted(new TxAction() {
            public void execute() {
                // 2nd: check if node for qualified name exists and has to be merged
                final AssociationKeeper attachedKeeper = controller.find(resource.getQualifiedName());
                if (attachedKeeper != null) {
                    merge(attachedKeeper, resource);
                } else {
                    // 3rd: if resource is really new, create a new Neo node.
                    persist(resource);
                }
            }
        }, controller);
    }

    @Override
    public void detach(final ResourceNode node) {
        assertActive();
        AssocKeeperAccess.getInstance().setAssociationKeeper(
                node, new DetachedAssociationKeeper(node.getAssociations()));
        controller.detach(node.getQualifiedName());
    }

    @Override
    public void remove(final ResourceID id) {
        assertActive();
        controller.remove(id.getQualifiedName());
    }

    @Override
    public void reset(final ResourceNode node) {
        assertActive();
        if (isAttached(node)) {
            return;
        }
        AssociationKeeper existing = controller.find(node.getQualifiedName());
        if (existing != null) {
            AssocKeeperAccess.getInstance().setAssociationKeeper(node, existing);
        } else {
            throw new IllegalStateException("Detached node cannot be reset.");
        }
    }

	// -- Statement operations ----------------------------

	@Override
	public void addStatement(final Statement stmt) {
		assertActive();
		final ResourceNode subject = resolve(stmt.getSubject());
		SNOPS.associate(subject, stmt.getPredicate(), stmt.getObject(), stmt.getContexts());
	}

	@Override
	public boolean removeStatement(final Statement stmt) {
		assertActive();
		final ResourceNode subject = resolve(stmt.getSubject());
		return SNOPS.remove(subject, stmt.getPredicate(), stmt.getObject());
	}

	// -- Semantic Graph operations -----------------------

	@Override
	public void attach(final SemanticGraph graph) {
		assertActive();
		tx().doTransacted(new TxResultAction<SemanticGraph>() {
            @Override
            public SemanticGraph execute() {
                for (Statement stmt : graph.getStatements()) {
                    final ResourceNode subject = resolve(stmt.getSubject());
                    SNOPS.associate(subject, stmt.getPredicate(), stmt.getObject(), stmt.getContexts());
                }
                return graph;
            }
        }, controller);
	}

	@Override
	public void detach(final SemanticGraph graph) {
		assertActive();
		for(SemanticNode node : graph.getNodes()){
			if (node.isResourceNode() && node.asResource().isAttached()){
				detach(node.asResource());
			}
		}
	}

	// -- Query support -----------------------------------

	@Override
	public Query createQuery() {
        assertActive();
		return new LuceneQueryBuilder(controller.getIndex(), getQNResolver());
	}

    @Override
    public Set<Statement> findIncomingStatements(ResourceID id) {
        throw new NotYetImplementedException();
    }

    // ----------------------------------------------------

	@Override
	public ConversationContext getConversationContext() {
		return controller.getConversationContext();
	}

    @Override
    public TransactionControl beginTransaction() {
        return controller.getTxProvider().begin().bind(controller);
    }

	@Override
	public void close() {
		controller.close();
	}

	// ----------------------------------------------------

    protected QNResolver getQNResolver() {
        return new QNResolver() {
            @Override
            public ResourceNode resolve(QualifiedName qn) {
                return ConversationImpl.this.resolve(SNOPS.id(qn));
            }
        };
    }

    // ----------------------------------------------------

    /**
     * Create the given resource node in Neo4j DB.
     * @param node A not yet persisted node.
     * @return The persisted ResourceNode.
     */
    protected ResourceNode persist(final ResourceNode node) {
        // 1st: create a corresponding Neo node and attach the Resource with the current controller.
        AssociationKeeper keeper = controller.create(node.getQualifiedName());

        // 2nd: retain copy of current associations
        final Set<Statement> copy = node.getAssociations();
        AssocKeeperAccess.getInstance().setAssociationKeeper(node, keeper);

        // 3rd: store all associations.
        for (Statement assoc : copy) {
            keeper.addAssociation(assoc);
        }

        return node;
    }

    /**
     * Merges all associations from the 'changed' node to the 'attached' keeper and put's keeper in 'changed'.
     * @param attached The currently attached keeper for this resource.
     * @param changed An unattached node referencing the same resource.
     */
    protected void merge(final AssociationKeeper attached, final ResourceNode changed) {
        final AssociationKeeper detached = AssocKeeperAccess.getInstance().getAssociationKeeper(changed);
        AssocKeeperAccess.getInstance().merge(attached, detached);
        AssocKeeperAccess.getInstance().setAssociationKeeper(changed, attached);
        controller.attach(changed.getQualifiedName(), (AttachedAssociationKeeper) attached);
    }

    /**
     * Check if the given node is attached to this conversation.
     */
    protected boolean isAttached(ResourceNode resource) {
        AssociationKeeper given = AssocKeeperAccess.getInstance().getAssociationKeeper(resource);
        ConversationContext givenCtx = given.getConversationContext();
        return givenCtx != null && givenCtx.equals(getConversationContext());
    }

    protected void assertActive() {
        if (!controller.isActive()) {
            throw new IllegalStateException("Conversation already closed.");
        }
    }

    protected void verifySameContext(ResourceNode resource) {
        AssociationKeeper given = AssocKeeperAccess.getInstance().getAssociationKeeper(resource);
        ConversationContext ctx = controller.getConversationContext();
        if (!given.getConversationContext().equals(ctx)) {
            LOGGER.warn("Resource {} is not in current conversation {}: ", resource, ctx);
        }
    }

    private TxProvider tx() {
        return controller.getTxProvider();
    }

}
