/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.index;

import java.util.ArrayList;
import java.util.List;

import org.arastreju.bindings.neo4j.impl.NeoDataStore;
import org.arastreju.bindings.neo4j.impl.TxAction;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.neo4j.graphdb.Node;
import org.neo4j.index.IndexHits;
import org.neo4j.index.IndexService;
import static org.arastreju.sge.SNOPS.*;

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
public class ResourceIndex {
	
	private final NeoDataStore store;
	private final IndexService service;

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param store The neo data store.
	 */
	public ResourceIndex(final NeoDataStore store) {
		this.store = store;
		this.service = store.getIndexService();
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
	public NeoDataStore getStore() {
		return store;
	}
	
	// -----------------------------------------------------
	
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
			public void execute(final NeoDataStore store) {
				final IndexService index = store.getIndexService();
				final IndexHits<Node> nodes = index.getNodes(key, value);
				for (Node node : nodes) {
					result.add(store.findResource(node));
				}
			}
		});
		return result;
	}
	

}
