/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.structure;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
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
public class OrderedNodes implements Iterable<ResourceNode> {

	/** 
	 * {@inheritDoc}
	 */
	public Iterator<ResourceNode> iterator() {
		return null;
	}
	
	@SuppressWarnings("unused")
	private ResourceNode findFirst(final Collection<ResourceNode> decls) {
		if (decls.isEmpty()) {
			return null;
		}
		ensureCorrectOrder(decls);
		final Set<ResourceNode> all = new HashSet<ResourceNode>(decls);
		final Set<ResourceNode> successors = new HashSet<ResourceNode>();
		for (ResourceNode current : all) {
			final ResourceNode successor = getSuccessor(current);
			if (successors != successor) {
				successors.add(successor);
			}
		}
		all.removeAll(successors);
		return all.iterator().next();
	}
	
	private void ensureCorrectOrder(final Collection<ResourceNode> decls) {
		final Set<ResourceNode> all = new HashSet<ResourceNode>(decls);
		final Set<ResourceNode> successors = new HashSet<ResourceNode>();
		for (ResourceNode current : all) {
			final ResourceNode successor = getSuccessor(current);
			if (successors.contains(successor)) {
				throw new IllegalStateException("Order of PropertyDeclarations has been corrupted!");
			} else if (null != successor) {
				successors.add(successor);
			}
		}
		all.removeAll(successors);
		if (all.isEmpty()) {
			throw new IllegalStateException("Order of PropertyDeclarations has been corrupted! First element not found.");
		} else if (all.size() > 1) {
			throw new IllegalStateException("Order of PropertyDeclarations has been corrupted! More than one first found: " + all);
		}
	}
	
	private ResourceNode getSuccessor(ResourceNode node) {
		final SemanticNode successor = SNOPS.singleObject(node, Aras.IS_PREDECESSOR_OF);
		if (successor != null && successor.isResourceNode()) {
			return successor.asResource();
		} else {
			return null;
		}
	}

}
