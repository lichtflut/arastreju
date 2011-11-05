/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query.exp;

import java.util.Collections;
import java.util.List;

import org.arastreju.sge.query.QueryExpression;
import org.arastreju.sge.query.QueryOperator;
import org.arastreju.sge.query.QueryParam;

/**
 * <p>
 * [DESCRIPTION]
 * </p>
 * 
 * <p>
 * Created Nov 4, 2011
 * </p>
 * 
 * @author Oliver Tigges
 */
public abstract class AbstractQueryExpression implements QueryExpression {

	private final QueryOperator queryOperator;

	// -----------------------------------------------------

	/**
	 * Constructor.
	 * 
	 * @param queryOperator
	 */
	public AbstractQueryExpression(final QueryOperator queryOperator) {
		this.queryOperator = queryOperator;
	}

	// -----------------------------------------------------

	/** 
	 * {@inheritDoc}
	 */
	public QueryOperator getOperator() {
		return queryOperator;
	}

	/** 
	 * {@inheritDoc}
	 */
	public QueryParam getQueryParam() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	public List<QueryExpression> getChildren() {
		return Collections.emptyList();
	}
	
	public abstract void add(final QueryExpression expr);
	
	// -----------------------------------------------------
	
	public static And and() {
		return new And();
	}
	
	public static Or or() {
		return new Or();
	}
	
	public static Not not() {
		return new Not();
	}
	
	public static LeafQueryExpression leaf(QueryParam param) {
		return new LeafQueryExpression(param);
	}
	
	// -----------------------------------------------------

	static class And extends GroupedQueryExpression {
		public And() {
			super(QueryOperator.AND);
		}
	}
	
	static class Or extends GroupedQueryExpression {
		public Or() {
			super(QueryOperator.OR);
		}
	}
	
	static class Not extends GroupedQueryExpression {
		public Not() {
			super(QueryOperator.NOT);
		}
		public Not(final AbstractQueryExpression expr) {
			super(QueryOperator.NOT, expr);
		}
	}

}
