/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.index;

import static org.arastreju.sge.SNOPS.uri;

import java.util.ArrayList;
import java.util.List;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.impl.ResourceResolver;
import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.IndexManager;

/**
 * <p>
 *  Wrapper around the Neo {@link IndexService} with convenience methods.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ResourceIndex implements NeoConstants {
	
	private final ResourceResolver resolver;
	
	private final NeoIndex neoIndex;

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param store The neo data store.
	 */
	public ResourceIndex(final SemanticNetworkAccess store, final IndexManager service) {
		this.resolver = store;
		this.neoIndex = new NeoIndex(store, service);
	}
	
	// -- LOOKUP ------------------------------------------
	
	/**
	 * Find in Index by key and value.
	 */
	public Node lookup(final QualifiedName qn) {
		return neoIndex.lookup(qn);
	}
	
	// -----------------------------------------------------
	
	/**
	 * Find in Index by key and value.
	 */
	public List<ResourceNode> lookup(final ResourceID predicate, final ResourceID value) {
		return lookup(uri(predicate), uri(value));
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public List<ResourceNode> lookup(final ResourceID predicate, final String value) {
		return lookup(uri(predicate), value);
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public List<ResourceNode> lookup(final String key, final String value) {
		return map(neoIndex.lookup(key, value));
	}
	
	// -- SEARCH ------------------------------------------
	
	/**
	 * Search in URI index by serach term. 
	 */
	public List<ResourceNode> searchById(final String searchTerm) {
		return map(neoIndex.searchById(searchTerm));
	}
	
	/**
	 * Search in value index by serach term.
	 */
	public List<ResourceNode> searchByValue(final String searchTerm) {
		return map(neoIndex.searchByValue(searchTerm));
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public List<ResourceNode> search(final String key, final String value) {
		return map(neoIndex.search(key, value));
	}
	
	// -- ADD TO INDEX ------------------------------------
	
	public void index(Node subject, ResourceID predicate, SemanticNode value) {
		neoIndex.index(subject, predicate, value);
	}
	
	public void index(Node subject, ValueNode value) {
		neoIndex.index(subject, value);
	}
	
	public void index(Node subject, ResourceID resourceID) {
		neoIndex.index(subject, resourceID);
	}
	
	// --REMOVE FROM INDEX --------------------------------
	
	public void remove(final Node node) {
		neoIndex.remove(node);
	}

	/**
	 * Remove relationship from index.
	 * @param rel The relationship to be removed.
	 */
	public void remove(final Relationship rel) {
		neoIndex.remove(rel);
	}
	
	// -----------------------------------------------------
	
	private List<ResourceNode> map(final List<Node> neoNodes) {
		final List<ResourceNode> result = new ArrayList<ResourceNode>(neoNodes.size());
		for (Node node : neoNodes) {
			result.add(resolver.resolveResource(node));
		}
		return result;
	}
	
}
