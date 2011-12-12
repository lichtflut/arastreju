/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query.exp;

import org.arastreju.sge.query.QueryExpression;
import org.arastreju.sge.query.QueryParam;

/**
 * <p>
 *  Leaf in query expression tree.
 * </p>
 *
 * <p>
 * 	Created Nov 8, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
class LeafQueryExpression extends AbstractQueryExpression {

	private final QueryParam param;
	
	// -----------------------------------------------------

	/**
	 * Constructor.
	 * @param param
	 */
	public LeafQueryExpression(final QueryParam param) {
		super(param.getOperator());
		this.param = param;
	}

	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public boolean isLeaf() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public QueryParam getQueryParam() {
		return param;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void add(final QueryExpression expr) {
		throw new UnsupportedOperationException("Can't append a query expression to a leaf. Mayb you forget a 'and'?");
	}
	
	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return param.toString();
	}

}