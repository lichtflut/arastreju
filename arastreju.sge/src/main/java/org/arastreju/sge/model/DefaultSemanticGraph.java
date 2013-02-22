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
package org.arastreju.sge.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.SimpleNamespace;

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
	
	private final Set<Statement> statements = new TreeSet<Statement>(new StmtComparator());
	
	// -----------------------------------------------------
	
	/**
	 * Creates an empty graph. 
	 */
	public DefaultSemanticGraph() {
	}
	
	/**
	 * Constructor based on associations.
	 */
	public DefaultSemanticGraph(final Collection<Statement> stmts){
		this.statements.addAll(stmts);
	}
	
	/**
	 * Constructor based on a root node.
	 */
	public DefaultSemanticGraph(final ResourceNode root) {
		addCascading(root, new HashSet<ResourceNode>());
	}
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public Set<Statement> getStatements() {
		return Collections.unmodifiableSet(statements);
	}


    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Statement> iterator() {
        return getStatements().iterator();
    }

	/**
	 * {@inheritDoc}
	 */
	public Set<SemanticNode> getNodes() {
		final Set<SemanticNode> nodes = new HashSet<SemanticNode>();
		for(Statement assoc : statements){
			nodes.add(assoc.getSubject());
			nodes.add(assoc.getObject());
		}
		return nodes;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<ResourceNode> getSubjects() {
		final Set<ResourceNode> subjects = new HashSet<ResourceNode>();
		for(Statement assoc : statements){
			subjects.add(assoc.getSubject().asResource());
		}
		return subjects;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<Namespace> getNamespaces() {
		final Set<Namespace> namespaces = new HashSet<Namespace>();
		for (Statement assoc : statements) {
			addNamespace(assoc.getSubject(), namespaces);
			addNamespace(assoc.getPredicate(), namespaces);
			addNamespace(assoc.getObject(), namespaces);
		}
		return namespaces;
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public void addStatements(final Collection<? extends Statement> stmts) {
		this.statements.addAll(stmts);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void addStatement(Statement stmt) {
		this.statements.add(stmt);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void merge(final SemanticGraph graph) {
		addStatements(graph.getStatements());
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (Statement assoc : statements) {
			sb.append(" -- " + assoc.toString() + "\n");
		}
		return sb.toString();
	}
	
	// -----------------------------------------------------
	
	private void addNamespace(final SemanticNode node, final Set<Namespace> targetSet){
		if (node.isResourceNode() && !node.asResource().isBlankNode()){
			targetSet.add(
					new SimpleNamespace(node.asResource().getQualifiedName().getNamespace()));
		} 
	}
	
	private void addCascading(final ResourceNode node, final Set<ResourceNode> visited) {
		visited.add(node);
		this.statements.addAll(node.getAssociations());
		for (SemanticNode object : SNOPS.objects(node.getAssociations())) {
			if (object.isResourceNode() && !visited.contains(object)) {
				addCascading(object.asResource(), visited);
			}
		}
	}

    private static class StmtComparator implements Comparator<Statement> {

		/** 
		* {@inheritDoc}
		*/
		public int compare(Statement stmt1, Statement stmt2) {
			int c = compare(stmt1.getSubject(), stmt2.getSubject());
			if (c == 0) {
				c = compare(stmt1.getPredicate(), stmt2.getPredicate());
			} 
			if (c == 0) {
				c = compare(stmt1.getObject(), stmt2.getObject());
			}
			return c;
		}
		
		private int compare(SemanticNode sn1, SemanticNode sn2) {
			if (sn1.isResourceNode()) {
				if (sn2.isResourceNode()) {
					return compare(sn1.asResource(), sn2.asResource());
				} else {
					return -1;
				}
			} else {
				if (sn2.isResourceNode()) {
					return 1;
				} else {
					return compare(sn1.asValue(), sn2.asValue());
				}
			}
		}
		
		private int compare(ResourceNode rn1, ResourceNode rn2) {
			return rn1.getQualifiedName().compareTo(rn2.getQualifiedName());
		}
		
		private int compare(ValueNode v1, ValueNode v2) {
			return v1.getStringValue().compareTo(v2.getStringValue());
		}
		
	}

}
