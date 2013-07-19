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
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.model.nodes.views.SNContext;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.ContextResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static final SNContext[] NO_CTX = new SNContext[0];

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationContextImpl.class);

    private static long ID_GEN = 0;

    // ----------------------------------------------------

    private final long ctxId = ++ID_GEN;

    private final Map<QualifiedName, AttachedAssociationKeeper> register = new HashMap<>();

    private final Set<SNContext> readContexts = new HashSet<>();

    private SNContext primaryContext;

    private boolean active = true;

    private boolean strict = false;

    private ContextResolver resolver;

    // ----------------------------------------------------

    /**
     * Constructor.
     */
    public ConversationContextImpl() {
    }

    // ----------------------------------------------------

    /**
     * Get the unique conversation ID.
     * @return The unique ID of the conversation.
     */
    public long getID() {
        return ctxId;
    }

    // ----------------------------------------------------

    @Override
    public SNContext getPrimaryContext() {
        return primaryContext;
    }

    @Override
    public SNContext[] getReadContexts() {
        if (readContexts != null) {
            return readContexts.toArray(new SNContext[readContexts.size()]);
        } else {
            return NO_CTX;
        }
    }

    @Override
    public ConversationContext setPrimaryContext(Context ctx) {
        assertActive();
        readContexts.remove(primaryContext);
        if (ctx != null) {
            primaryContext = resolver.resolve(ctx);
            readContexts.add(primaryContext);
        } else {
            primaryContext = null;
        }
        return this;
    }

    @Override
    public ConversationContext setReadContexts(Context... ctxs) {
        assertActive();
        this.readContexts.clear();
        if (ctxs != null) {
            addAllResolved(readContexts, ctxs);
        }
        if (primaryContext != null) {
            readContexts.add(resolver.resolve(primaryContext));
        }
        return this;
    }

    @Override
    public boolean isStrictContextRegarding() {
        return strict;
    }

    @Override
    public ConversationContext setStrictContextRegarding(boolean strict) {
        assertActive();
        this.strict = strict;
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

    public void setContextResolver(ContextResolver resolver) {
        this.resolver = resolver;
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

    // ----------------------------------------------------

    private void addAllResolved(Set<SNContext> readContexts, Context[] ctxs) {
        for (Context ctx : ctxs) {
            readContexts.add(resolver.resolve(ctx));
        }
    }

}
