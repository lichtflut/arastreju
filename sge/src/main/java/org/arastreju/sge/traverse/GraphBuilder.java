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
import org.arastreju.sge.model.DefaultSemanticGraph;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  Generic builder of a semantic graph based on traversal rules.
 * </p>
 *
 * <p>
 * 	Created Jan 26, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class GraphBuilder {
	
	private final TraversalFilter filter;
	
	private final SemanticGraph graph = new DefaultSemanticGraph();

	// ----------------------------------------------------
	
	/**
	 * @param filter
	 */
	public GraphBuilder(TraversalFilter filter) {
		this.filter = filter;
	}
	
	/**
	 * Constructor.
	 */
	public GraphBuilder() {
		this(new AcceptAllFilter());
	}
	
	// ----------------------------------------------------
	
	/**
	 * @return the graph
	 */
	public SemanticGraph getGraph() {
		return graph;
	}
	
	// ----------------------------------------------------

	public GraphBuilder addCascading(ResourceNode node) {
		addCascading(node, new HashSet<ResourceNode>());
		return this;
	}
	
	/**
	 * @param associations
	 */
	public void addStatements(Set<Statement> associations) {
		graph.addStatements(associations);
	}
	
	// ----------------------------------------------------

	private void addCascading(final ResourceNode node, final Set<ResourceNode> visited) {
		visited.add(node);
		final Set<Statement> associations = node.getAssociations();
		for (Statement statement : associations) {
			switch (filter.accept(statement)) {
			case ACCEPT:
				graph.addStatement(statement);
				break;
			case ACCEPPT_CONTINUE:
				graph.addStatement(statement);
				for (SemanticNode object : SNOPS.objects(node.getAssociations())) {
					if (object.isResourceNode() && !visited.contains(object)) {
						addCascading(object.asResource(), visited);
					}
				}
			case STOP:
			default:
				break;
			}
		}
	}

}
