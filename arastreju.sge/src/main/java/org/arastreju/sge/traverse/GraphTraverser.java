/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.traverse;

import java.util.HashSet;
import java.util.Set;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  Common traverser for graphs.
 * </p>
 *
 * <p>
 * 	Created Feb 28, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class GraphTraverser implements StatementVisitor {
	
	private final TraversalFilter filter;
	
	private final StatementVisitor visitor;

	// ----------------------------------------------------
	
	/**
	 * @param filter
	 */
	public GraphTraverser(TraversalFilter filter) {
		this.filter = filter;
		this.visitor = this;
	}
	
	/**
	 * @param filter
	 * @param visitor
	 */
	public GraphTraverser(TraversalFilter filter, StatementVisitor visitor) {
		this.filter = filter;
		this.visitor = visitor;
	}



	// ----------------------------------------------------
	
	public void start(ResourceNode... node) {
		final HashSet<ResourceNode> visited = new HashSet<ResourceNode>();
		for (ResourceNode current : node) {
			traverse(current, visited);
		}
	}
	
	// ----------------------------------------------------
	
	public void visit(Statement stmt) {
	}
	
	// ----------------------------------------------------
	
	private void traverse(final ResourceNode node, final Set<ResourceNode> visited) {
		visited.add(node);
		final Set<Statement> associations = node.getAssociations();
		for (Statement statement : associations) {
			switch (filter.accept(statement)) {
			case ACCEPT:
				visitor.visit(statement);
				break;
			case ACCEPPT_CONTINUE:
				visitor.visit(statement);
				for (SemanticNode object : SNOPS.objects(node.getAssociations())) {
					if (object.isResourceNode() && !visited.contains(object)) {
						traverse(object.asResource(), visited);
					}
				}
			case STOP:
			default:
				break;
			}
		}
	}

}
