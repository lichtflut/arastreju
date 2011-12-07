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
package org.arastreju.bindings.neo4j.impl;

import java.lang.reflect.Field;

import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.neo4j.graphdb.Node;

/**
 * <p>
 *  Accessor for {@link AssociationKeeper} of a Resource Node. 
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class AssocKeeperAccess {
	
	private final Field assocKeeperField;
	
	private static final AssocKeeperAccess INSTANCE = new AssocKeeperAccess();
	
	// -----------------------------------------------------
	
	/**
	 * Get the association keeper of given node.
	 */
	public static AssociationKeeper getAssociationKeeper(final ResourceNode node){
		final ResourceNode resource = node.asResource();
		if (!(resource instanceof SNResource)){
			throw new IllegalArgumentException("Cannot get AssociationKeeper for class: " + node.getClass());
		}
		try {
			return (AssociationKeeper) INSTANCE.assocKeeperField.get(resource);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot get AssociationKeeper", e);
		}
	}
	
	/**
	 * Get the neo association keeper of given node.
	 */
	public static NeoAssociationKeeper getNeoAssociationKeeper(final ResourceNode node){
		return (NeoAssociationKeeper) getAssociationKeeper(node);
	}
	
	/**
	 * Get the Neo4j node attached to the Arastreju node's association keeper.
	 */
	public static Node getNeoNode(final ResourceNode node){
		try {
			NeoAssociationKeeper keeper = (NeoAssociationKeeper) getAssociationKeeper(node);
			return keeper.getNeoNode();
		} catch (ClassCastException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Set the given association keeper to the resource node.
	 * @param node Must be an instance of {@link SNResource}.
	 * @param ak The association keeper to be set. 
	 */
	public static void setAssociationKeeper(final ResourceNode node, final AssociationKeeper ak) {
		final ResourceNode resource = node.asResource();
		if (!(resource instanceof SNResource)){
			throw new IllegalArgumentException("Cannot set AssociationKeeper for class: " + node.getClass());
		}
		try {
			INSTANCE.assocKeeperField.set(resource, ak);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot get AssociationKeeper", e);
		}
	}
	
	// -----------------------------------------------------

	/**
	 * Private constructor.
	 */
	private AssocKeeperAccess() {
		try {
			assocKeeperField = SNResource.class
					.getDeclaredField("associationKeeper");
			assocKeeperField.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
}

}
