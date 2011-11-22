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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.Namespace;

/**
 * <p>
 *  Default implementation of a semantic graph.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class DefaultSemanticGraph implements SemanticGraph {
	
	private final Set<Association> associations = new HashSet<Association>();
	
	// -----------------------------------------------------
	
	/**
	 * Creates an empty graph. 
	 */
	public DefaultSemanticGraph() {
	}
	
	/**
	 * Constructor based on associations.
	 */
	public DefaultSemanticGraph(final Collection<Association> associations){
		this.associations.addAll(associations);
	}
	
	/**
	 * Constructor based on a root node.
	 */
	public DefaultSemanticGraph(final ResourceNode root) {
		addCascading(root, new HashSet<ResourceNode>());
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.SemanticGraph#getAssociations()
	 */
	public Set<Association> getAssociations() {
		return Collections.unmodifiableSet(associations);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.SemanticGraph#getNodes()
	 */
	public Set<SemanticNode> getNodes() {
		final Set<SemanticNode> nodes = new HashSet<SemanticNode>();
		for(Association assoc : associations){
			nodes.add(assoc.getSubject());
			nodes.add(assoc.getObject());
		}
		return nodes;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.SemanticGraph#getSubjects()
	 */
	public Set<ResourceNode> getSubjects() {
		final Set<ResourceNode> subjects = new HashSet<ResourceNode>();
		for(Association assoc : associations){
			subjects.add(assoc.getSubject());
		}
		return subjects;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.SemanticGraph#getNamespaces()
	 */
	public Collection<Namespace> getNamespaces() {
		final Set<Namespace> namespaces = new HashSet<Namespace>();
		for (Association assoc : associations) {
			addNamespace(assoc.getSubject(), namespaces);
			addNamespace(assoc.getPredicate(), namespaces);
			addNamespace(assoc.getObject(), namespaces);
		}
		return namespaces;
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.SemanticGraph#addAssociations(java.util.Collection)
	 */
	public void addAssociations(final Collection<Association> associations) {
		this.associations.addAll(associations);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.SemanticGraph#merge(org.arastreju.sge.model.SemanticGraph)
	 */
	public void merge(final SemanticGraph graph) {
		addAssociations(graph.getAssociations());
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (Association assoc : associations) {
			sb.append(" -- " + assoc.toString() + "\n");
		}
		return sb.toString();
	}
	
	// -----------------------------------------------------
	
	private void addNamespace(final SemanticNode node, final Set<Namespace> targetSet){
		if (node.isResourceNode() && !node.asResource().isBlankNode()){
			targetSet.add(node.asResource().getQualifiedName().getNamespace());
		} 
	}
	
	private void addCascading(final ResourceNode node, final Set<ResourceNode> visited) {
		visited.add(node);
		this.associations.addAll(node.getAssociations());
		for (SemanticNode object : SNOPS.objects(node.getAssociations())) {
			if (object.isResourceNode() && !visited.contains(object)) {
				addCascading(object.asResource(), visited);
			}
		}
	}

}
