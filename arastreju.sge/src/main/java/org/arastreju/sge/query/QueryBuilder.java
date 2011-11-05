/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

import java.util.Stack;

import org.arastreju.sge.query.exp.AbstractQueryExpression;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Nov 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class QueryBuilder  {
	
	private Stack<QueryExpression> stack = new Stack<QueryExpression>();

	// -----------------------------------------------------
	
	public QueryBuilder() {
	}
	
	// -----------------------------------------------------
	
	public Query query() {
		return new QueryImpl(stack.firstElement());
	}
	
	public Query simple(final QueryParam param) {
		return add(param).query();
	}
	
	// -----------------------------------------------------
	
	public QueryBuilder add(final QueryParam param) {
		return append(param);
	}

	public QueryBuilder and() {
		return prepend(AbstractQueryExpression.and());
	}
	
	public QueryBuilder or() {
		return prepend(AbstractQueryExpression.or());
	}
	
	public QueryBuilder not() {
		return append(AbstractQueryExpression.not());
	}
	
	public QueryBuilder beginAnd() {
		return append(AbstractQueryExpression.and());
	}
	
	public QueryBuilder beginOr() {
		return append(AbstractQueryExpression.or());
	}
	
	public QueryBuilder end() {
		if (stack.size() > 1) {
			stack.pop();
		}
		return this;
	}
	
	// -----------------------------------------------------
	
	protected QueryBuilder append(final QueryParam param) {
		final QueryExpression exp = AbstractQueryExpression.leaf(param);
		if (stack.isEmpty()) {
			stack.push(exp);
		} else {
			stack.peek().add(exp);
		}
		return this;
	}
	
	protected QueryBuilder append(final QueryExpression exp) {
		if (!stack.isEmpty()) {
			stack.peek().add(exp);
		}
		stack.push(exp);
		return this;
	}
	
	protected QueryBuilder prepend(final QueryExpression exp) {
		if (stack.isEmpty()) {
			throw new IllegalArgumentException("Nothing to combine with expression: " + exp);
		} else {
			final QueryExpression current = stack.pop();
			exp.add(current);
			stack.push(exp);
		}
		return this;
	}
	
	// -----------------------------------------------------
	
	private class QueryImpl implements Query {

		private final QueryExpression root;

		/**
		 * @param root The root expression.
		 */
		public QueryImpl(final QueryExpression root) {
			this.root = root;
		}
		
		/**
		 * @return the root
		 */
		public QueryExpression getRoot() {
			return root;
		}

	}
	
}
