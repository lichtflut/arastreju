/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * <p>
 *  Remover of Neo Nodes from datastore.
 * </p>
 *
 * <p>
 * 	Created Sep 21, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NodeRemover {
	
	private final ResourceRegistry registry;
	
	// -----------------------------------------------------

	/**
	 * Constructor.
	 * @param registry The registry.
	 */
	public NodeRemover(final ResourceRegistry registry) {
		this.registry = registry;
	}
	
	// -----------------------------------------------------

	/**
	 * Remove the given node.
	 * @param node The node.
	 * @param cascade Flag if removing shall be cascaded.
	 * @return The set of removed nodes.
	 */
	public Set<Node> remove(final Node neoNode, final boolean cascade) {
		final Set<Node> deleted = new HashSet<Node>();
		remove(neoNode, deleted, cascade);
		return deleted;
	}
	
	/**
	 * Remove the given node.
	 * @param node The node.
	 * @param cascade Flag if removing shall be cascaded.
	 * @return The set of removed nodes.
	 */
	public Set<Node> remove(final ResourceNode node, final boolean cascade) {
		return remove(AssocKeeperAccess.getNeoNode(node), cascade);
	}
	
	// -----------------------------------------------------
	
	private void remove(final Node neoNode, final Set<Node> deleted, final boolean cascade) {
		// 1st: detach Arastreju Node
		detachArastrejuNode(neoNode);
		
		// 2nd: delete relations
		final List<Node> cascading = new ArrayList<Node>();
		for (Relationship rel : neoNode.getRelationships()) {
			cascading.add(rel.getEndNode());
			rel.delete();
		}
		
		// 3rd: delete neo node
		neoNode.delete();
		deleted.add(neoNode);

		// 4th: cascade
		if (cascade) {
			for(Node c : cascading) {
				if (!deleted.contains(c) && !c.hasRelationship(Direction.INCOMING)) {
					remove(c, deleted, cascade);
				}
			}
		}
	}
	
	private void detachArastrejuNode(final Node neoNode) {
		if (neoNode.hasProperty(NeoConstants.PROPERTY_URI)) {
			final QualifiedName qn = new QualifiedName(neoNode.getProperty(NeoConstants.PROPERTY_URI).toString());
			if (registry.contains(qn)){
				final ResourceNode node = registry.get(qn);
				registry.unregister(node);
				AssocKeeperAccess.setAssociationKeeper(node, new DetachedAssociationKeeper());
			}
		}
	}

}
