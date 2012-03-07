/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

/**
 * <p>
 *  A sub query, that will be included literally as is into the outer query.
 * </p>
 *
 * <p>
 * 	Created Jan 20, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class SubQuery implements QueryParam {

	private final String query;
	
	// ----------------------------------------------------
	
	/**
	 * @param query
	 */
	public SubQuery(final String query) {
		this.query = query;
	}
	
	// ----------------------------------------------------

	/** 
	 * {@inheritDoc}
	 */
	public QueryOperator getOperator() {
		return QueryOperator.SUB_QUERY;
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getName() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	public Object getValue() {
		return query;
	}
	
	// ----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return query;
	}

}
