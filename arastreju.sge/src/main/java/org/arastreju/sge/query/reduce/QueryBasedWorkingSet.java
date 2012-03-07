/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query.reduce;

import java.util.Iterator;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;

/**
 * <p>
 *  Working Set based on a query.
 * </p>
 *
 * <p>
 * 	Created Mar 6, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class QueryBasedWorkingSet implements WorkingSet {

	private final String id;
	
	private QueryResult result;
	
	private Query query;
	
	// ----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param id The working set ID.
	 */
	public QueryBasedWorkingSet(String id, Query query) {
		this.id = id;
		this.query = query;
	}
	
	/**
	 * Constructor.
	 * @param id The working set ID.
	 */
	public QueryBasedWorkingSet(String id, QueryResult result) {
		this.id = id;
		this.result = result;
	}
	
	
	// ----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}


	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<ResourceNode> getNodes() {
		return getResult().iterator();
	}
	
	// ----------------------------------------------------
	
	private QueryResult getResult() {
		if (result == null) {
			result = query.getResult();
		}
		return result;
	}

}
