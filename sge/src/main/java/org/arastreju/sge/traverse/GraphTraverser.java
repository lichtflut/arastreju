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
package org.arastreju.sge.traverse;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

import java.util.HashSet;
import java.util.Set;

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
