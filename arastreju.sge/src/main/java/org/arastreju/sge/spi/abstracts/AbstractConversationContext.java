/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * The Arastreju-Neo4j binding is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxAction;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.persistence.TxResultAction;
import org.arastreju.sge.spi.GraphDataConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
public abstract class AbstractConversationContext implements WorkingContext {

	public static final Context[] NO_CTX = new Context[0];

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConversationContext.class);

	private static long ID_GEN = 0;

	// ----------------------------------------------------

    private final long ctxId = ++ID_GEN;

    private final GraphDataConnection connection;

    private final Map<QualifiedName, AttachedAssociationKeeper> register = new HashMap<QualifiedName, AttachedAssociationKeeper>();

    private final Set<Context> readContexts = new HashSet<Context>();

	private Context primaryContext;

	private boolean active = true;

	// ----------------------------------------------------

	/**
	 * Creates a new Working Context.
	 */
	public AbstractConversationContext(GraphDataConnection connection) {
		LOGGER.debug("New Conversation Context startet. " + ctxId);
        this.connection = connection;
        connection.register(this);
	}

    /**
     * Creates a new Working Context.
     */
    public AbstractConversationContext(GraphDataConnection connection, Context primaryContext, Context... readContexts) {
        this(connection);
        setPrimaryContext(primaryContext);
        setReadContexts(readContexts);
    }

	// ----------------------------------------------------

    @Override
    public AttachedAssociationKeeper lookup(QualifiedName qn) {
        assertActive();
        AttachedAssociationKeeper registered = register.get(qn);
        if (registered != null && !registered.isAttached()) {
            LOGGER.warn("There is a detached AssociationKeeper in the conversation register: {}.", qn);
        }
        return registered;
    }

    @Override
    public AttachedAssociationKeeper find(QualifiedName qn) {
        assertActive();
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
        assertActive();
        AttachedAssociationKeeper keeper = getTxProvider().doTransacted(new TxResultAction<AttachedAssociationKeeper>() {
            @Override
            public AttachedAssociationKeeper execute() {
                return connection.create(qn);
            }
        });
        attach(qn, keeper);
        return keeper;
    }

    @Override
    public void remove(final QualifiedName qn) {
        assertActive();
        detach(qn);
        getTxProvider().doTransacted(new TxAction() {
            @Override
            public void execute() {
                connection.remove(qn);
            }
        });

    }

    // ----------------------------------------------------

    @Override
    public void attach(QualifiedName qn, AttachedAssociationKeeper keeper) {
        assertActive();
        register.put(qn, keeper);
        keeper.setConversationContext(this);
    }

    @Override
    public void detach(QualifiedName qn) {
        assertActive();
        final AttachedAssociationKeeper removed = register.remove(qn);
        if (removed != null) {
            removed.detach();
        }
    }

    // ----------------------------------------------------

    @Override
    public TxProvider getTxProvider() {
        return connection.getTxProvider();
    }

	/**
	 * Clear the cache.
	 */
    @Override
	public void clear() {
		assertActive();
		clearCaches();
	}

	/**
	 * Close and invalidate this context.
	 */
    @Override
	public void close() {
		if (active) {
			clear();
            onClose();
			active = false;
			LOGGER.info("Conversation will be closed. " + ctxId);
		}
	}

	/**
	 * @return the active
	 */
    public boolean isActive() {
		return active;
	}

    @Override
    public void onModification(QualifiedName qualifiedName, WorkingContext otherContext) {
        AttachedAssociationKeeper existing = lookup(qualifiedName);
        if (existing != null) {
            LOGGER.info("Concurrent change on node {} in other context {}.", qualifiedName, otherContext);
            existing.notifyChanged();
        }
    }

    @Override
    public IndexProvider getIndexProvider() {
        return connection.getIndexProvider();
    }

    // ----------------------------------------------------

	public Context[] getReadContexts() {
		assertActive();
		if (readContexts != null) {
			return readContexts.toArray(new Context[readContexts.size()]);
		} else {
			return NO_CTX;
		}
	}

    public Context getPrimaryContext() {
    	return primaryContext;
    }

    @Override
	public ConversationContext setPrimaryContext(Context ctx) {
		this.primaryContext = ctx;
        if (primaryContext != null) {
            readContexts.add(primaryContext);
        }
		return this;
	}

    @Override
	public ConversationContext setReadContexts(Context... ctxs) {
        this.readContexts.clear();
        if (ctxs != null) {
            Collections.addAll(readContexts, ctxs);
        }
        if (primaryContext != null) {
            readContexts.add(primaryContext);
        }
        return this;
	}

    // ----------------------------------------------------


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractConversationContext that = (AbstractConversationContext) o;
        return ctxId == that.ctxId;
    }

    @Override
    public int hashCode() {
        return (int) (ctxId ^ (ctxId >>> 32));
    }

    @Override
    public String toString() {
        return "ConversationContext[" +  ctxId + "]";
    }

    // ----------------------------------------------------

    protected GraphDataConnection getConnection() {
        return connection;
    }

    protected void onClose() {
        connection.unregister(this);
    }

    protected void clearCaches() {
        for (AttachedAssociationKeeper keeper : register.values()) {
            keeper.detach();
        }
        register.clear();
    }

	protected void assertActive() {
		if (!active) {
			LOGGER.warn("Conversation context already closed. " + ctxId);
			throw new IllegalStateException("ConversationContext already closed.");
		}
	}

	@Override
	protected void finalize() throws Throwable {
        super.finalize();
		if (active) {
			LOGGER.debug("Conversation context will be removed by GC, but has not been closed. " + ctxId);
		}
	}

}
