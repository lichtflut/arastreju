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
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
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
public abstract class AbstractConversationContext<T extends AssociationKeeper> implements WorkingContext<T> {

	public static final Context[] NO_CTX = new Context[0];

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConversationContext.class);

	private static long ID_GEN = 0;

	// ----------------------------------------------------

    private final long ctxId = ++ID_GEN;

    private final GraphDataConnection<T> connection;

    private final Map<QualifiedName, T> register = new HashMap<QualifiedName, T>();

    private final Set<Context> readContexts = new HashSet<Context>();

	private Context primaryContext;

	private boolean active = true;

	// ----------------------------------------------------

	/**
	 * Creates a new Working Context.
	 */
	public AbstractConversationContext(GraphDataConnection<T> connection) {
		LOGGER.debug("New Conversation Context startet. " + ctxId);
        this.connection = connection;
        connection.register(this);
	}

    /**
     * Creates a new Working Context.
     */
    public AbstractConversationContext(GraphDataConnection<T> connection, Context primaryContext, Context... readContexts) {
        this(connection);
        setPrimaryContext(primaryContext);
        setReadContexts(readContexts);
    }

	// ----------------------------------------------------

    /**
     * Lookup the qualified name in the register.
     * @param qn The qualified name.
     * @return The association keeper or null.
     */
    public T lookup(QualifiedName qn) {
        return register.get(qn);
    }

    /**
     * Find the resource in this conversation context or in underlying data store.
     * @param qn The resource's qualified name.
     * @return The association keeper or null.
     */
    public T find(QualifiedName qn) {
        assertActive();
        T registered = lookup(qn);
        if (registered != null) {
            return registered;
        }
        T existing = connection.find(qn);
        if (existing != null) {
            attach(qn, existing);
            return existing;
        } else {
            return null;
        }
    }

    /**
     * @param qn The resource's qualified name.
     * @return The association keeper or null;
     */
    public T create(final QualifiedName qn) {
        assertActive();
        T keeper = getTxProvider().doTransacted(new TxResultAction<T>() {
            @Override
            public T execute() {
                return connection.create(qn);
            }
        });
        attach(qn, keeper);
        return keeper;
    }

    /**
     * @param qn The resource's qualified name.
     * @param keeper The keeper to be accessed.
     */
    public void attach(QualifiedName qn, T keeper) {
        assertActive();
        register.put(qn, keeper);
        keeper.setConversationContext(this);
    }

    /**
     * @param qn The resource's qualified name.
     */
    public void detach(QualifiedName qn) {
        assertActive();
        final T removed = register.remove(qn);
        if (removed != null) {
            removed.detach();
        }
    }

    // ----------------------------------------------------

    public TxProvider getTxProvider() {
        return connection.getTxProvider();
    }

    public GraphDataConnection<T> getConnection() {
        return connection;
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

    protected void onClose() {
        connection.unregister(this);
    }

    protected void clearCaches() {
        for (T keeper : register.values()) {
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
