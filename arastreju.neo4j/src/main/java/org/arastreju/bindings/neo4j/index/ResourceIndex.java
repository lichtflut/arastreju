/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.index;

import static org.arastreju.sge.SNOPS.uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.SNResourceNeo;
import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.bindings.neo4j.mapping.NodeMapper;
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;

/**
 * <p>
 *  Wrapper around the Neo {@link IndexService} with convenience methods and a registry for caching.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ResourceIndex implements NeoConstants {
	
	private final NeoIndex neoIndex;
	
	private final NodeMapper mapper;
	
	private final Map<QualifiedName, ResourceNode> register = new HashMap<QualifiedName, ResourceNode>();
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param store The neo data store.
	 * @param txProvider The tx provider.
	 */
	public ResourceIndex(final SemanticNetworkAccess store, final IndexManager service, final TxProvider txProvider) {
		this.neoIndex = new NeoIndex(txProvider, service);
		this.mapper = new NodeMapper(store);
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode findResource(final QualifiedName qn) {
		if (register.containsKey(qn)){
			return register.get(qn);
		}
		final Node neoNode = lookup(qn);
		if (neoNode != null){
			final SNResourceNeo arasNode = new SNResourceNeo(qn);
			register(arasNode);
			mapper.toArasNode(neoNode, arasNode);
			return arasNode;
		} else {
			return null;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode resolveResource(final Node neoNode) {
		final QualifiedName qn = new QualifiedName(neoNode.getProperty(PROPERTY_URI).toString());
		if (register.containsKey(qn)){
			return register.get(qn);
		}
		final SNResourceNeo arasNode = new SNResourceNeo(qn);
		register(arasNode);
		mapper.toArasNode(neoNode, arasNode);
		return arasNode;
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
	
	public IndexHits<Node> search(final String query) {
		return neoIndex.search(query);
	}
	
	// -- ADD TO INDEX ------------------------------------
	
	public void index(Node subject, ResourceID predicate, SemanticNode value) {
		neoIndex.index(subject, predicate, value);
	}
	
	public void index(Node subject, ValueNode value) {
		neoIndex.index(subject, value);
	}
	
	public void index(final Node neoNode, final ResourceNode resourceNode) {
		neoIndex.index(neoNode, resourceNode);
		register(resourceNode);
	}
	
	// --REMOVE FROM INDEX --------------------------------
	
	public void remove(final Node node) {
		neoIndex.remove(node);
		if (node.hasProperty(NeoConstants.PROPERTY_URI)) {
			register.remove(new QualifiedName(node.getProperty(NeoConstants.PROPERTY_URI).toString()));
		}
	}

	/**
	 * Remove relationship from index.
	 * @param rel The relationship to be removed.
	 */
	public void remove(final Relationship rel) {
		neoIndex.remove(rel);
	}
	
	
	// -- CACHE/REGISTRY ----------------------------------
	
	/**
	 * @param node
	 */
	public void onDetach(final ResourceNode node) {
		register.remove(node.getQualifiedName());
	}
	
	public void clearCache(){
		register.clear();
	}
	
	// -----------------------------------------------------
	
	private void register(final ResourceNode resource){
		register.put(resource.getQualifiedName(), resource);
	}
	
	private List<ResourceNode> map(final List<Node> neoNodes) {
		final List<ResourceNode> result = new ArrayList<ResourceNode>(neoNodes.size());
		for (Node node : neoNodes) {
			result.add(resolveResource(node));
		}
		return result;
	}

}
