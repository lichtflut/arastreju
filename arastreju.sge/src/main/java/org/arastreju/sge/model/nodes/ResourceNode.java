/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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

import java.util.Set;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNProperty;

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
public interface ResourceNode extends SemanticNode, ResourceID {
	
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
	
	// -----------------------------------------------------

	/**
	 * Get all associations.
	 * @return Set of all associations.
	 */
	Set<Association> getAssociations();

	/**
	 * Selects all associations with the given predicate.
	 * @param predicate The relevant predicate.
	 * @return Set of matching associations.
	 */
	Set<Association> getAssociations(final ResourceID predicate);
	
	// -----------------------------------------------------

	/**
	 * Add an association.
	 * @param assoc The association to add.
	 */
	void addToAssociations(final Association assoc);
	
	/**
	 * Removes the association from this resource object. This will have no effect on the database when this node isn't
	 * attached!
	 * @param assoc The association to be removed.
	 * @return true if association has been removed.
	 */
	boolean remove(Association assoc);
	
	// -----------------------------------------------------

	SNEntity asEntity();

	SNClass asClass();

	SNProperty asProperty();

}