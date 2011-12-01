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
import org.arastreju.bindings.neo4j.mapping.NodeMapper;
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
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
	 * @param mapper The node mapper.
	 * @param neoIndex The index manager.
	 * @param txProvider The tx provider.
	 */
	public ResourceIndex(final NodeMapper mapper, final IndexManager neoIndex, final TxProvider txProvider) {
		this.neoIndex = new NeoIndex(txProvider, neoIndex);
		this.mapper = mapper;
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
			return createArasNode(neoNode, qn);
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
		return createArasNode(neoNode, qn);
	}

	protected SNResourceNeo createArasNode(final Node neoNode, final QualifiedName qn) {
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
	
	// -- SEARCH ------------------------------------------
	
	public IndexHits<Node> search(final String query) {
		return neoIndex.search(query);
	}
	
	// -- ADD TO INDEX ------------------------------------
	
	public void index(final Node neoNode, final Statement stmt) {
		if (stmt.getObject().isValueNode()) {
			final ValueNode value = stmt.getObject().asValue();
			neoIndex.index(neoNode, value);
			neoIndex.index(neoNode, stmt.getPredicate(), value);
		} else {
			neoIndex.index(neoNode, stmt.getPredicate(), stmt.getObject().asResource());
		}
	}
	
	public void index(final Node neoNode, final ResourceNode resourceNode) {
		neoIndex.index(neoNode, resourceNode.getQualifiedName());
		register(resourceNode);
	}
	
	// --REMOVE FROM INDEX --------------------------------
	
	public void removeFromIndex(final Node node) {
		neoIndex.remove(node);
		if (node.hasProperty(NeoConstants.PROPERTY_URI)) {
			register.remove(new QualifiedName(node.getProperty(NeoConstants.PROPERTY_URI).toString()));
		}
	}

	/**
	 * Remove relationship from index.
	 * @param rel The relationship to be removed.
	 */
	public void removeFromIndex(final Relationship rel) {
		neoIndex.remove(rel);
	}
	
	// -- CACHE/REGISTRY ----------------------------------
	
	/**
	 * @param node
	 */
	public void removeFromRegister(final ResourceNode node) {
		register.remove(node.getQualifiedName());
	}
	
	public void clearRegister(){
		register.clear();
	}
	
	// -----------------------------------------------------
	
	private void register(final ResourceNode resource){
		register.put(resource.getQualifiedName(), resource);
	}
	
	/**
	 * Find in Index by key and value.
	 */
	private List<ResourceNode> lookup(final String key, final String value) {
		return map(neoIndex.lookup(key, value));
	}
	
	private List<ResourceNode> map(final List<Node> neoNodes) {
		final List<ResourceNode> result = new ArrayList<ResourceNode>(neoNodes.size());
		for (Node node : neoNodes) {
			result.add(resolveResource(node));
		}
		return result;
	}

}
