/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.arastreju.bindings.neo4j.impl.NeoResourceResolver;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.IndexHits;

/**
 * <p>
 *  Result for a {@link Query}.
 * </p>
 *
 * <p>
 * 	Created Nov 10, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoQueryResult implements QueryResult {

	private final IndexHits<Node> hits;
	private final NeoResourceResolver resolver;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param hits The index hits.
	 */
	public NeoQueryResult(final IndexHits<Node> hits, final NeoResourceResolver resolver) {
		this.hits = hits;
		this.resolver = resolver;
	}
	
	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public Iterator<ResourceNode> iterator() {
		return new ResolvingIterator();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void close() {
		hits.close();
	}

	/** 
	 * {@inheritDoc}
	 */
	public int size() {
		return hits.size();
	}

	/** 
	 * {@inheritDoc}
	 */
	public List<ResourceNode> toList() {
		final List<ResourceNode> result = new ArrayList<ResourceNode>(size());
		for (Node node : hits) {
			result.add(resolver.resolve(node));
		}
		return result;
	}
	
	/** 
	* {@inheritDoc}
	*/
	public List<ResourceNode> toList(int max) {
		int absMax = Math.min(hits.size(), max);
		final List<ResourceNode> result = new ArrayList<ResourceNode>(absMax);
		for (int i=0; i < absMax; i++) {
			result.add(resolver.resolve(hits.next()));
		}
		hits.close();
		return result;
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return hits.size() <= 0;
	}
	
	/** 
	* {@inheritDoc}
	*/
	public ResourceNode getSingleNode() {
		if (isEmpty()) {
			return null;
		} else if (size() > 1) {
			throw new IllegalStateException("More than one result found.");
		} else {
			return toList().get(0);
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + size() + " results]";
	}
	
	// -----------------------------------------------------
	
	class ResolvingIterator implements Iterator<ResourceNode> {

		/** 
		 * {@inheritDoc}
		 */
		public boolean hasNext() {
			return hits.hasNext();
		}

		/** 
		 * {@inheritDoc}
		 */
		public ResourceNode next() {
			return resolver.resolve(hits.next());
		}

		/** 
		 * {@inheritDoc}
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
