/*
 * Copyright (C) Risk Management Solutions GmbH
 */
package org.arastreju.bindings.neo4j.impl;

import java.lang.reflect.Field;

import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.neo4j.graphdb.Node;

/**
 * [Description]
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
		if (!(node instanceof SNResource)){
			throw new IllegalArgumentException("Cannot get AssociationKeeper for class: " + node.getClass());
		}
		try {
			return (AssociationKeeper) INSTANCE.assocKeeperField.get(node);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot get AssociationKeeper", e);
		}
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
		if (!(node instanceof SNResource)){
			throw new IllegalArgumentException("Cannot set AssociationKeeper for class: " + node.getClass());
		}
		try {
			INSTANCE.assocKeeperField.set(node, ak);
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
