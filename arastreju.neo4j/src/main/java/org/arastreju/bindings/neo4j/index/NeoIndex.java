/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.index;

import static org.arastreju.sge.SNOPS.uri;

import java.util.ArrayList;
import java.util.List;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.tx.TxAction;
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.bindings.neo4j.tx.TxResultAction;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class NeoIndex implements NeoConstants {
	
	/**
	 * Index for resources.
	 */
	public static final String INDEX_RESOURCES = "resources";
	
	// -----------------------------------------------------
	
	/**
	 * Index key representing a resource'id.
	 */
	public static final String INDEX_KEY_RESOURCE_URI = "resource-uri";
	
	/**
	 * Index key for a resource's value. 
	 */
	public static final String INDEX_KEY_RESOURCE_VALUE = "resource-value";
	
	// -----------------------------------------------------
	
	private final TxProvider txProvider;
	private final IndexManager manager;
	
	private final Logger logger = LoggerFactory.getLogger(NeoIndex.class);

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param store The neo data store.
	 */
	public NeoIndex(final TxProvider txProvider, final IndexManager service) {
		this.txProvider = txProvider;
		this.manager = service;
	}
	
	// -- LOOKUP ------------------------------------------
	
	/**
	 * Find in Index by key and value.
	 */
	public Node lookup(final QualifiedName qn) {
		return manager.forNodes(INDEX_RESOURCES).get(INDEX_KEY_RESOURCE_URI, qn.toURI()).getSingle();
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public List<Node> lookup(final ResourceID predicate, final ResourceID value) {
		return lookup(uri(predicate), uri(value));
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public List<Node> lookup(final ResourceID predicate, final String value) {
		return lookup(uri(predicate), value);
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public List<Node> lookup(final String key, final String value) {
		final List<Node> result = new ArrayList<Node>();
		txProvider.doTransacted(new TxAction() {
			public void execute() {
				toList(result, resourceIndex().get(key, value));
			}
		});
		return result;
	}
	
	// -- SEARCH ------------------------------------------
	
	/**
	 * Search in value index by serach term.
	 */
	public List<Node> searchByValue(final String searchTerm) {
		return search(INDEX_KEY_RESOURCE_VALUE, searchTerm);
	}
	
	/**
	 * Execute the query.
	 * @param query The query.
	 * @return The resulting index hits.
	 */
	public IndexHits<Node> search(final String query) {
		return txProvider.doTransacted(new TxResultAction<IndexHits<Node>>() {
			public IndexHits<Node> execute() {
				return resourceIndex().query(query);
			}
		});
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public List<Node> search(final String key, final String value) {
		final List<Node> result = new ArrayList<Node>();
		txProvider.doTransacted(new TxAction() {
			public void execute() {
				toList(result, resourceIndex().query(key, value));
			}
		});
		return result;
	}
	
	// -- ADD TO INDEX ------------------------------------
	
	public void index(Node subject, ResourceID predicate, ValueNode value) {
		resourceIndex().add(subject, uri(predicate), value.getStringValue());
	}
	
	public void index(Node subject, ResourceID predicate, ResourceNode value) {
		resourceIndex().add(subject, uri(predicate), uri(value));	
	}
	
	public void index(Node subject, ValueNode value) {
		resourceIndex().add(subject, INDEX_KEY_RESOURCE_VALUE, value.asValue().getStringValue());
	}
	
	public void index(Node subject, QualifiedName qn) {
		resourceIndex().add(subject, INDEX_KEY_RESOURCE_URI, qn.toURI());
	}
	
	// --REMOVE FROM INDEX --------------------------------
	
	public void remove(final Node node) {
		resourceIndex().remove(node);
	}

	/**
	 * Remove relationship from index.
	 * @param rel The relationship to be removed.
	 */
	public void remove(final Relationship rel) {
		resourceIndex().remove(rel.getStartNode(), (String) rel.getProperty(PREDICATE_URI));
	}
	
	// -----------------------------------------------------
	
	/**
	 * @param result
	 * @param nodes
	 */
	private void toList(final List<Node> result, final IndexHits<Node> nodes) {
		for (Node node : nodes) {
			if (node.hasProperty(PROPERTY_URI)) {
				result.add(node);
			} else {
				logger.error("Invalid node in index, will be removed: " + node);
				remove(node);
				for(Relationship rel : node.getRelationships()) {
					remove(rel);
				}
			}
		}
	}
	
	private Index<Node> resourceIndex() {
		return manager.forNodes(INDEX_RESOURCES);
	}

}
