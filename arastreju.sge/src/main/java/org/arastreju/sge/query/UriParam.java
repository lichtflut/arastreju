/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

/**
 * <p>
 *  Parameter for query nodes by their URI.
 * </p>
 *
 * <p>
 * 	Created Nov 8, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class UriParam implements QueryParam {

	private final String term;
	
	// -----------------------------------------------------
	
	/**
	 * @param term
	 */
	public UriParam(final String term) {
		this.term = term;
	}
	
	
	// -----------------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	public QueryOperator getOperator() {
		return QueryOperator.HAS_URI;
	};
	
	/** 
	 * {@inheritDoc}
	 */
	public String getName() {
		return "URI";
	}

	/** 
	 * {@inheritDoc}
	 */
	public Object getValue() {
		return term;
	}

}
