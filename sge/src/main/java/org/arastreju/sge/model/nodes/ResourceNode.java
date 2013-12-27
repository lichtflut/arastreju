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
package org.arastreju.sge.model.nodes;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.StatementMetaInfo;

import java.util.Set;

/**
 * <p>
 *  Base interface of all nodes in a semantic network representing a Resource.
 * </p>
 *
 * <p>
 * 	Created Dec 1, 2009
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ResourceNode extends ResourceID {
	
	/**
	 * Check if this node is a blank node.
	 * @return true if this is a blank node.
	 */
	boolean isBlankNode();
	
	/**
	 * States if this node is attached to a managed semantic model.
	 * @return true if the node is attached.
	 */
	boolean isAttached();
	
	// -- ASSOCIATIONS ------------------------------------

	/**
	 * Get all associations of this node.
	 * @return Set of all associations.
	 */
	Set<Statement> getAssociations();

	// -----------------------------------------------------

	/**
	 * Add an association.
	 * @param predicate The predicate of the association.
     * @param object The object.
	 * @return The created statement.
	 */
	Statement addAssociation(ResourceID predicate, SemanticNode object);

    /**
     * Add an association.
     * @param predicate The predicate of the association.
     * @param object The object.
     * @param metaInfo The meta information for this statement.
     * @return The created statement.
     */
    Statement addAssociation(ResourceID predicate, SemanticNode object, StatementMetaInfo metaInfo);

	/**
	 * Removes the association from this resource object. This will have no effect on the database when this node isn't
	 * attached!
	 * @param stmt The association to be removed.
	 * @return true if association has been removed.
	 */
	boolean removeAssociation(Statement stmt);
	
}