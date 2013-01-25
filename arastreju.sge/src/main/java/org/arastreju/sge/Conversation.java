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

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.query.Query;

import java.util.Set;

/**
 * <p>
 *  A Modeling Conversation allows to read an change parts of the semantic model.
 * </p>
 *
 * <p>
 * 	Created Sep 1, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Conversation {
	
	/**
	 * Adds a statement to the semantic model.
	 * @param stmt The statement.
	 * @return The attached association.
	 */
	void addStatement(Statement stmt);
	
	/**
	 * Removed a statement from the semantic model.
	 * @param stmt The statement.
	 * @return boolean indicating if a statement has been removed.
	 */
	boolean removeStatement(Statement stmt);
	
	// -- QUERY -------------------------------------------
	
	/**
	 * Creates a new query.
	 * @return The query.
	 */
	Query createQuery();

    /**
     * Find all statements where given resource ID is the object.
     * @param object The object of the statements.
     * @return The statements.
     */
    Set<Statement> findIncomingStatements(ResourceID object);
	
	// -- RESOURCE NODE -----------------------------------
	
	/**
	 * Tries to find an existing resource with given qualified name. The returned node will be attached.
	 * If no resource is found null will be returned. 
	 * @param qn The qualified name.
	 * @return The corresponding resource or <code>null</code>.
	 */
	ResourceNode findResource(QualifiedName qn);
	
	/**
	 * Resolves the resource referenced by given resource identifier. The returned node will be attached.
	 * If there does not exist a corresponding resource, it will be created.
	 * Thus there will never be returned null. 
	 * @param resourceID The resource identifier.
	 * @return The corresponding resource node.
	 */
	ResourceNode resolve(ResourceID resourceID);
	
	/**
	 * Attach the resource to this conversation.
	 * All changes to the attached node will immediately be persistent.
     * If the node is attached to another conversation, it will be reattached here.
	 * @param node The node to be attached.
	 * @return The attached node.
	 */
	void attach(ResourceNode node);
	
	/**
	 * Detach the given node from the conversation.
	 * All following changes will not affect the underlying store.
	 * @param node The node to be detached.
	 */
	void detach(ResourceNode node);
	
	/**
	 * Reset a detached node to the state in the persisted state.
	 * If the node is attached this call will have no effect.
	 * @param node The node to be reseted.
	 */
	void reset(ResourceNode node);
	
	/**
	 * Removes the resource identified by given ID and all of it's incoming
	 * and outgoing associations. 
	 * @param id The ID of the resource to be removed.
	 */
	void remove(ResourceID id);
	
	// -- SEMANTIC GRAPH ----------------------------------
	
	/**
	 * Attach all statements of the graph.
	 * @param graph The graph to be attached.
	 */
	void attach(SemanticGraph graph);
	
	/**
	 * Detach all statements of the graph.
	 * @param graph The graph to be detached.
	 */
	void detach(SemanticGraph graph);
	
	// -----------------------------------------------------
	
	/**
	 * Get the context of the current conversation.
	 * @return The conversation context.
	 */
	ConversationContext getConversationContext();
	
	/**
	 * Begins a new transaction and returns the corresponding control object.
	 * @return The tx control object.
	 */
	TransactionControl beginTransaction();
	
	/**
	 * Finish the conversation and release all resources.
	 */
	void close();

}
