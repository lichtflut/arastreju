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
public abstract class AbstractConversationContext implements ConversationContext {

	public static final Context[] NO_CTX = new Context[0];

	private static final Logger logger = LoggerFactory.getLogger(AbstractConversationContext.class);

	private static long ID_GEN = 0;

	// ----------------------------------------------------

	private Context writeContext;

	private Context[] readContexts;

	private boolean active = true;

	private final long ctxId = ++ID_GEN;

	// ----------------------------------------------------

	/**
	 * Creates a new Working Context.
	 */
	public AbstractConversationContext() {
		logger.info("New Conversation Context startet. " + ctxId);
	}

	// ----------------------------------------------------
	
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
	public void close() {
		if (active) {
			clear();
			active = false;
			logger.info("Conversation will be closed. " + ctxId);
		}
	}
	
	/**
	 * @return the active
	 */
    public boolean isActive() {
		return active;
	}
	
	// ----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public Context[] getReadContexts() {
		assertActive();
		if (readContexts != null) {
			return readContexts;
		} else {
			return NO_CTX;
		}
	}
	
   /** 
	 * {@inheritDoc}
	 */
    public Context getPrimaryContext() {
    	return writeContext;
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	public ConversationContext setPrimaryContext(Context ctx) {
		this.writeContext = ctx;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
	public ConversationContext setReadContexts(Context... ctxs) {
		this.readContexts = ctxs;
		return this;
	}

    // ----------------------------------------------------

    @Override
    public String toString() {
        return "ConversationContext[" +  ctxId + "]";
    }

    // ----------------------------------------------------

    protected abstract void clearCaches();

    // ----------------------------------------------------
	
	protected void assertActive() {
		if (!active) {
			logger.warn("Conversation context already closed. " + ctxId);
			throw new IllegalStateException("ConversationContext already closed.");
		}
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected void finalize() throws Throwable {
		if (active) {
			logger.warn("Conversation context will be removed by GC, but has not been closed. " + ctxId);
		}
	}
}
