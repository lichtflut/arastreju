/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

/**
 * <p>
 *  Parameter for query nodes by one of their values.
 * </p>
 *
 * <p>
 * 	Created Nov 8, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ValueParam implements QueryParam {

	private final String term;
	
	// -----------------------------------------------------
	
	/**
	 * @param term
	 */
	public ValueParam(final String term) {
		this.term = term;
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public QueryOperator getOperator() {
		return QueryOperator.HAS_VALUE;
	};
	
	/** 
	 * {@inheritDoc}
	 */
	public String getName() {
		return "VALUE";
	}

	/** 
	 * {@inheritDoc}
	 */
	public Object getValue() {
		return term;
	}

}
