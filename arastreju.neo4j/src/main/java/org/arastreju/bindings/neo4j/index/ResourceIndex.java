/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.index;

import static org.arastreju.sge.SNOPS.uri;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.impl.NeoResourceResolver;
import org.arastreju.bindings.neo4j.query.NeoQueryResult;
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.QueryResult;
import org.neo4j.graphdb.Node;
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
	
	private final AttachedResourcesCache cache = new AttachedResourcesCache();

	private final NeoResourceResolver resolver;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param assocHandler The assoc handler.
	 * @param indexManager The index manager.
	 * @param txProvider The tx provider.
	 */
	public ResourceIndex(final NeoResourceResolver resolver, final IndexManager indexManager, final TxProvider txProvider) {
		this.resolver = resolver;
		this.neoIndex = new NeoIndex(txProvider, indexManager);
	}
	
	// -----------------------------------------------------
	
	/**
	 * Find Arastreju node by qualified name.
	 */
	public ResourceNode findResourceNode(final QualifiedName qn) {
		return cache.get(qn);
	}
	
	/**
	 * Find Neo node by qualified name.
	 */
	public Node findNeoNode(final QualifiedName qn) {
		return neoIndex.lookup(qn);
	}
	
	// -----------------------------------------------------
	
	/**
	 * Find in Index by key and value.
	 */
	public QueryResult lookup(final ResourceID predicate, final ResourceID value) {
		final IndexHits<Node> hits = lookup(uri(predicate), uri(value));
		return new NeoQueryResult(hits, resolver);
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public QueryResult lookup(final ResourceID predicate, final String value) {
		final IndexHits<Node> hits = lookup(uri(predicate), value);
		return new NeoQueryResult(hits, resolver);
	}
	
	// -- SEARCH ------------------------------------------
	
	public QueryResult search(final String query) {
		return new NeoQueryResult(neoIndex.search(query), resolver);
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
	
	/**
	 * Re-index a node.
	 * @param neoNode The Neo node.
	 * @param resourceNode The corresponding Arastreju node.
	 */
	public void reindex(final Node neoNode, final ResourceNode resourceNode) {
		removeFromIndex(neoNode);
		neoIndex.index(neoNode, resourceNode.getQualifiedName());
		for (Statement stmt : resourceNode.getAssociations()) {
			index(neoNode, stmt);
		}
	}
	
	// --REMOVE FROM INDEX --------------------------------
	
	public void removeFromIndex(final Node node) {
		neoIndex.remove(node);
		if (node.hasProperty(NeoConstants.PROPERTY_URI)) {
			cache.remove(new QualifiedName(node.getProperty(NeoConstants.PROPERTY_URI).toString()));
		}
	}

	/**
	 * Remove relationship from index.
	 * @param rel The relationship to be removed.
	 */
	public void removeFromIndex(final Node neoNode, final Statement stmt) {
		final String key = stmt.getPredicate().getQualifiedName().toURI();
		if (stmt.getObject().isValueNode()) {
			final ValueNode value = stmt.getObject().asValue();
			// TODO: Remove general value without predicate.
			neoIndex.remove(neoNode,key, value.getStringValue());
		} else {
			neoIndex.remove(neoNode, key, stmt.getObject().asResource().getQualifiedName().toURI());
		}
	}
	
	// -- REGISTER ----------------------------------
	
	public void register(final ResourceNode resource){
		cache.put(resource.getQualifiedName(), resource);
	}
	
	/**
	 * @param node
	 */
	public void removeFromRegister(final ResourceNode node) {
		cache.remove(node.getQualifiedName());
	}
	
	public void clearRegister(){
		cache.clear();
	}
	
	// -----------------------------------------------------
	
	/**
	 * Find in Index by key and value.
	 */
	private IndexHits<Node> lookup(final String key, final String value) {
		return neoIndex.lookup(key, value);
	}
	
}
