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

import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;

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

	@SuppressWarnings("unchecked")
	public static <T extends ResourceNode> List<T> sortResources(Collection<T> nodes) {
		if (nodes.size() < 2) {
			return new ArrayList<T>(nodes);
		}
		final ResourceNode initial = nodes.iterator().next();
		ResourceNode predecessor = getPredecessor(initial);
		ResourceNode successor = getSuccessor(initial);
		
		if (predecessor == null && successor == null) {
			return new ArrayList<T>(nodes);
		} else if (predecessor == null) {
			return (List<T>) sortBySuccessors(nodes);
		} else if (successor == null) {
			return (List<T>) sortByPredecessors(nodes);
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
			return (List<T>) result;
		}
	}
	
	// ----------------------------------------------------
	
	public static List<ResourceNode> sortByPredecessors(Collection<? extends ResourceNode> nodes) {
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

	public static List<ResourceNode> sortBySuccessors(Collection<? extends ResourceNode> nodes) {
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
				return (ResourceNode) stmt.getObject();
			}
		}
		return null;
	}
	
	protected static ResourceNode getPredecessor(ResourceNode node) {
		for (Statement	stmt : node.getAssociations()) {
			if (Aras.IS_SUCCESSOR_OF.equals(stmt.getPredicate())) {
				return (ResourceNode) stmt.getObject();
			}
		}
		return null;
	}
	
	private static <T extends ResourceNode> T findFirst(final Collection<T> decls) {
		if (decls.isEmpty()) {
			return null;
		}
		final Set<T> all = new HashSet<T>(decls);
		for (ResourceNode current : decls) {
			all.remove(getSuccessor(current));
		}
		return all.iterator().next();
	}
	
	private static <T extends ResourceNode> T findLast(final Collection<T> decls) {
		if (decls.isEmpty()) {
			return null;
		}
		final Set<T> all = new HashSet<T>(decls);
		for (ResourceNode current : decls) {
			all.remove(getPredecessor(current));
		}
		return all.iterator().next();
	}

}
