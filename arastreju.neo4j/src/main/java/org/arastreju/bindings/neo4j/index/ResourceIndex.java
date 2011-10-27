/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.index;

import static org.arastreju.sge.SNOPS.uri;

import java.util.ArrayList;
import java.util.List;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.bindings.neo4j.tx.TxAction;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.index.IndexHits;
import org.neo4j.index.IndexService;
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
public class ResourceIndex implements NeoConstants {
	
	private final SemanticNetworkAccess store;
	private final IndexService service;
	
	private final Logger logger = LoggerFactory.getLogger(ResourceIndex.class);

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param store The neo data store.
	 */
	public ResourceIndex(final SemanticNetworkAccess store, final IndexService service) {
		this.store = store;
		this.service = service;
	}
	
	// -----------------------------------------------------
	
	/**
	 * @return the service
	 */
	public IndexService getIndexService() {
		return service;
	}
	
	/**
	 * @return the store
	 */
	public SemanticNetworkAccess getStore() {
		return store;
	}
	
	// -----------------------------------------------------
	
	/**
	 * Find in Index by key and value.
	 */
	public Node lookup(final QualifiedName qn) {
		return service.getSingleNode(INDEX_KEY_RESOURCE_URI, qn.toURI());
	}
	
	// -----------------------------------------------------
	
	/**
	 * Search in URI index by serach term. 
	 */
	public List<ResourceNode> searchById(final String searchTerm) {
		return lookup(INDEX_KEY_RESOURCE_URI, searchTerm);
	}
	
	/**
	 * Search in value index by serach term.
	 */
	public List<ResourceNode> searchByValue(final String searchTerm) {
		return lookup(INDEX_KEY_RESOURCE_VALUE, searchTerm);
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public List<ResourceNode> lookup(final ResourceID key, final ResourceID value) {
		return lookup(uri(key), uri(value));
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public List<ResourceNode> lookup(final ResourceID key, final String value) {
		return lookup(uri(key), value);
	}
	
	/**
	 * Find in Index by key and value.
	 */
	public List<ResourceNode> lookup(final String key, final String value) {
		final List<ResourceNode> result = new ArrayList<ResourceNode>();
		store.doTransacted(new TxAction() {
			public void execute(final SemanticNetworkAccess store) {
				final IndexHits<Node> nodes = service.getNodes(key, value);
				for (Node node : nodes) {
					if (node.hasProperty(PROPERTY_URI)) {
						result.add(store.resolveResource(node));
					} else {
						logger.error("Invalid node in index, will be removed: " + node);
						remove(node);
						for(Relationship rel : node.getRelationships()) {
							remove(rel);
						}
					}
				}
			}
		});
		return result;
	}
	
	// -----------------------------------------------------
	
	public void index(Node subject, ResourceID predicate, SemanticNode value) {
		if (value.isResourceNode()) {
			service.index(subject, uri(predicate), uri(value.asResource()));	
		} else {
			service.index(subject, uri(predicate), value.asValue().getStringValue());
		}
	}
	
	public void index(Node subject, ValueNode value) {
		service.index(subject, INDEX_KEY_RESOURCE_VALUE, value.asValue().getStringValue());
	}
	
	public void index(Node subject, ResourceID resourceID) {
		service.index(subject, INDEX_KEY_RESOURCE_URI, uri(resourceID));
	}
	
	// -----------------------------------------------------
	
	public void remove(final Node node) {
		service.removeIndex(node, INDEX_KEY_RESOURCE_URI);
		service.removeIndex(node, INDEX_KEY_RESOURCE_VALUE);
	}

	/**
	 * Remove relationship from index.
	 * @param rel The relationship to be removed.
	 */
	public void remove(final Relationship rel) {
		service.removeIndex(rel.getStartNode(), (String) rel.getProperty(PREDICATE_URI));
	}

}
