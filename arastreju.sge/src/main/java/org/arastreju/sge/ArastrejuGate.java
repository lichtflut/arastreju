/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
import org.arastreju.sge.organize.Organizer;

/**
 * <p>
 *  Single point of entrance to Arastreju.
 * </p>
 *
 * <p>
 * 	Created Sep 1, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ArastrejuGate {

	/**
	 * Starts a new Modeling Conversation allowing to read and change the model.
	 * @return The modeling conversation.
	 */
	Conversation startConversation();

    /**
     * Starts a new Modeling Conversation using the given contexts.
     * @param primary The primary context in which new statements will be placed.
     * @param readContexts The additional contexts to be regarded when traversing through the graph.
     * @return The modeling conversation.
     */
    Conversation startConversation(Context primary, Context... readContexts);
	
	// ----------------------------------------------------
	
	/**
     * Close the gate and free all resources.
     */
    void close();
	
}
