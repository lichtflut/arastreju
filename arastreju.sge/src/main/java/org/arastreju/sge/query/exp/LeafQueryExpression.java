/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query.exp;

import org.arastreju.sge.query.QueryExpression;
import org.arastreju.sge.query.QueryOperator;
import org.arastreju.sge.query.QueryParam;

class LeafQueryExpression extends AbstractQueryExpression {

	private final QueryParam param;

	/**
	 * @param queryOperator
	 * @param param
	 */
	public LeafQueryExpression(final QueryParam param) {
		super(QueryOperator.EQUALS);
		this.param = param;
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
		throw new UnsupportedOperationException("Can't add a query expression to a leaf.");
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return param.toString();
	}

}