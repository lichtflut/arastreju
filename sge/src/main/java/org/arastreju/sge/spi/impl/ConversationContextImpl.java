package org.arastreju.sge.spi.impl;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  Implementation of conversation context.
 * </p>
 *
 * <p>
 *  Created 16.05.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class ConversationContextImpl implements ConversationContext {

    public static final Context[] NO_CTX = new Context[0];

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationContextImpl.class);

    private static long ID_GEN = 0;

    // ----------------------------------------------------

    private final long ctxId = ++ID_GEN;

    private final Map<QualifiedName, AttachedAssociationKeeper> register = new HashMap<QualifiedName, AttachedAssociationKeeper>();

    private final Set<Context> readContexts = new HashSet<Context>();

    private Context primaryContext;

    private boolean active = true;

    // ----------------------------------------------------

    public long getID() {
        return ctxId;
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

    // -- Register Access ---------------------------------

    public AttachedAssociationKeeper get(QualifiedName qn) {
        assertActive();
        return register.get(qn);
    }

    public void put(QualifiedName qn, AttachedAssociationKeeper keeper) {
        assertActive();
        register.put(qn, keeper);
    }

    public AttachedAssociationKeeper remove(QualifiedName qn) {
        assertActive();
        return register.remove(qn);
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

    public boolean isActive() {
        return active;
    }

    // ----------------------------------------------------

    /**
     * Close and invalidate this context.
     */
    public void close() {
        clear();
        active = false;
        LOGGER.info("Conversation '{}' will be closed.", ctxId);
    }

    public void assertActive() {
        if (!active) {
            LOGGER.warn("Conversation context already closed: {}", ctxId);
            throw new IllegalStateException("ConversationContext already closed.");
        }
    }

    public void clearCaches() {
        for (AttachedAssociationKeeper keeper : register.values()) {
            keeper.detach();
        }
        register.clear();
    }

    // ----------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationContextImpl that = (ConversationContextImpl) o;
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

}
