/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  Structure for ordered nodes.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class LinkedOrderedNodes {

	public static List<ResourceNode> sortResources(Collection<ResourceNode> nodes) {
		if (nodes.size() < 2) {
			return new ArrayList<ResourceNode>(nodes);
		}
		final ResourceNode initial = nodes.iterator().next();
		ResourceNode predecessor = getPredecessor(initial);
		ResourceNode successor = getSuccessor(initial);
		
		if (predecessor == null && successor == null) {
			throw new IllegalStateException("Node is not linked");
		} else if (predecessor == null) {
			return sortBySuccessors(nodes);
		} else if (successor == null) {
			return sortByPredecessors(nodes);
		} else {
			final LinkedList<ResourceNode> result = new LinkedList<ResourceNode>();
			result.add(initial);
			while(predecessor != null) {
				result.addFirst(predecessor);
				predecessor = getPredecessor(predecessor);
			}
			while(successor != null) {
				result.addLast(successor);
				successor = getSuccessor(successor);
			}
			return result;
		}
	}
	
	// ----------------------------------------------------
	
	/**
	 * @param nodes
	 * @return 
	 */
	private static List<ResourceNode> sortByPredecessors(Collection<ResourceNode> nodes) {
		final LinkedList<ResourceNode> result = new LinkedList<ResourceNode>();
		ResourceNode current = findLast(nodes);
		while (current != null) {
			result.addFirst(current);
			current = getPredecessor(current);
		}
		if (result.size() != nodes.size()) {
			throw new IllegalStateException("List is not well ordered.");
		}
		return result;
	}

	/**
	 * @param nodes
	 * @return 
	 */
	private static List<ResourceNode> sortBySuccessors(Collection<ResourceNode> nodes) {
		final List<ResourceNode> result = new ArrayList<ResourceNode>(nodes.size());
		ResourceNode current = findFirst(nodes);
		while (current != null) {
			result.add(current);
			current = getSuccessor(current);
		}
		if (result.size() != nodes.size()) {
			throw new IllegalStateException("List is not well ordered.");
		}
		return result;
	}
	
	protected static ResourceNode getSuccessor(ResourceNode node) {
		for (Statement	stmt : node.getAssociations()) {
			if (Aras.IS_PREDECESSOR_OF.equals(stmt.getPredicate())) {
				return stmt.getObject().asResource();
			}
		}
		return null;
	}
	
	protected static ResourceNode getPredecessor(ResourceNode node) {
		final SemanticNode successor = SNOPS.singleObject(node, Aras.IS_SUCCESSOR_OF);
		if (successor != null && successor.isResourceNode()) {
			return successor.asResource();
		} else {
			return null;
		}
	}
	
	private static ResourceNode findFirst(final Collection<ResourceNode> decls) {
		if (decls.isEmpty()) {
			return null;
		}
		final Set<ResourceNode> all = new HashSet<ResourceNode>(decls);
		for (ResourceNode current : decls) {
			all.remove(getSuccessor(current));
		}
		return all.iterator().next();
	}
	
	private static ResourceNode findLast(final Collection<ResourceNode> decls) {
		if (decls.isEmpty()) {
			return null;
		}
		final Set<ResourceNode> all = new HashSet<ResourceNode>(decls);
		for (ResourceNode current : decls) {
			all.remove(getPredecessor(current));
		}
		return all.iterator().next();
	}

}
