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
import org.arastreju.sge.model.nodes.views.SNPropertyDeclaration;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;

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
	
	String getName();
	
	void setName(String name);
	
	Namespace getNamespace();
	
	void setNamespace(Namespace namespace);
	
	QualifiedName getQualifiedName();
	
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
	
	/**
	 * Selects all associations with the given predicate and returns their
	 * client objects.
	 * @param predicate The relevant predicate.
	 * @return Set of the clients of the matching associations.
	 */
	Set<SemanticNode> getAssociationClients(final ResourceID predicate);
	
	/**
	 * Get an association with given predicate.
	 * @param type The relevant predicate.
	 * @return The first matching association.
	 */
	Association getSingleAssociation(final ResourceID predicate);
	
	/**
	 * Get the client of single association with given predicate.
	 * @param type The relevant predicate.
	 * @return The first matching association's client.
	 */
	SemanticNode getSingleAssociationClient(final ResourceID predicate);

	// -----------------------------------------------------

	boolean hasAssociation(final Association assoc);

	void addToAssociations(final Association assoc);
	
	// -----------------------------------------------------

	/**
	 * Revoke the association. It will be detached from this resource node, but will be
	 * remembered as revoked. If you want to remove the Association directly use remove() instead.
	 * @param assoc The association to be revoked.
	 */
	boolean revoke(Association assoc);

	/**
	 * Removes the association from this resource object. This may have no effect on the database when you store this node. 
	 * If you want to remove an association from the database either use the corresponding service or revoke() the association
	 * and store the node.
	 * @param assoc The association to be removed.
	 */
	void remove(Association assoc);
	
	/**
	 * Revert all transient changes made to this node.
	 */
	void reset();
	
	// -----------------------------------------------------

	SNEntity asEntity();

	SNClass asClass();

	SNProperty asProperty();

	SNPropertyDeclaration asPropertyDeclaration();

}