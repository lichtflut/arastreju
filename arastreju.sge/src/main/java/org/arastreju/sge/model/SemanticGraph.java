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
package org.arastreju.sge.model;

import java.util.Collection;
import java.util.Set;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.Namespace;


/**
 * <p>
 *  A Semantic Graph is a graph based on Semantic Nodes and Associations which connect
 *  these nodes.
 * </p>
 *
 * <p>
 * 	Created Aug 27, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface SemanticGraph {
	
	/**
	 * Get all associations of this graph.
	 * @return The associations.
	 */
	Collection<Statement> getStatements();
	
	/**
	 * Get all nodes of the graph.
	 * @return The nodes.
	 */
	Set<SemanticNode> getNodes();
	
	/**
	 * Get all subjects, i.e. all nodes that are the subject in at least one association. 
	 * @return The subjects.
	 */
	Set<ResourceNode> getSubjects();
	
	/**
	 * Get all namespaces.
	 * @return The namespaces.
	 */
	Collection<Namespace> getNamespaces();
	
	// -----------------------------------------------------
	
	/**
	 * Add some statements.
	 */
	void addStatements(Collection<? extends Statement> associations);
	
	/**
	 * Merge all data from given graph into this graph. 
	 * The given graph will not be changed.
	 * @param graph The graph to be merged.
	 */
	void merge(SemanticGraph graph);
	
}
