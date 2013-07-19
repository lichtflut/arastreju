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
package org.arastreju.sge;

import org.arastreju.sge.context.Context;

/**
 * <p>
 *  Context of a {@link Conversation}.
 * </p>
 *
 * <p>
 * 	Created Jun 7, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ConversationContext {

    /**
     * Get the current primary context.
     * @return The primary context.
     */
    Context getPrimaryContext();

    /**
     * Get the current contexts to be read when loading and traversing graph data, includes the primary context.
     * @return All contexts to be read.
     */
	Context[] getReadContexts();

    /**
     * Check if 'strict' context regarding has been enabled.
     * @return true if in 'strict' mode.
     */
    boolean isStrictContextRegarding();

    // ----------------------------------------------------

    /**
     * Set the primary context operating in. New statements without explicit context set, will be attached to
     * this primary context. The primary context will also always be a read context.
     * @param context The primary context.
     * @return This.
     */
	ConversationContext setPrimaryContext(Context context);

    /**
     * Set the contexts to be read when loading and traversing graph data.
     * @param contexts The read contexts to use.
     * @return This.
     */
    ConversationContext setReadContexts(Context... contexts);

    /**
     * Specify if context regarding shall be performed in a strict way.
     * In strict mode, only the given read contexts will be regarded for reading and traversing of graphs.
     * Access contexts and visibility (public) will be ignored.
     * Default is non strict.
     * @param strict Flag indicating if strict.
     * @return This.
     */
    ConversationContext setStrictContextRegarding(boolean strict);

    // ----------------------------------------------------

    /**
     * Clear all cached data of this context.
     */
	void clear();

    /**
     * Check if this context is still open and active.
     * @return true if active.
     */
	boolean isActive();

}