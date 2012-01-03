/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

import java.util.Arrays;

/**
 * <p>
 *  Criteria for sorting of queries.
 * </p>
 *
 * <p>
 * 	Created Jan 3, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class SortCriteria {
	
	private final String[] columns;
	
	// ----------------------------------------------------
	
	/**
	 * Constructor.
	 */
	public SortCriteria(final String... columns) {
		if (columns == null) {
			throw new IllegalArgumentException("columns may not be null.");
		}
		this.columns = columns;
	}
	
	// ----------------------------------------------------
	
	/**
	 * @return the columns
	 */
	public String[] getColumns() {
		return columns;
	}
	
	// ----------------------------------------------------
	
	/** 
	* {@inheritDoc}
	*/
	@Override
	public String toString() {
		return Arrays.toString(columns);
	}

}
