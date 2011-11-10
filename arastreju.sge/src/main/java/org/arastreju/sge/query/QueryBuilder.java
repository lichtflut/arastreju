/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

import java.util.Stack;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.query.exp.AbstractQueryExpression;

/**
 * <p>
 *  Basic implementation of {@link Query}.
 * </p>
 *
 * <p>
 * 	Created Nov 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class QueryBuilder implements Query {
	
	private Stack<QueryExpression> stack = new Stack<QueryExpression>();

	// -----------------------------------------------------
	
	/**
	 * Default constructor.
	 */
	public QueryBuilder() {}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public abstract QueryResult getResult();
	
	/** 
	 * {@inheritDoc}
	 */
	public abstract ResourceNode getSingleNode();
	
	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public QueryBuilder add(final QueryParam param) {
		return append(param);
	}

	/** 
	 * {@inheritDoc}
	 */
	public QueryBuilder and() {
		return prepend(AbstractQueryExpression.and());
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public QueryBuilder or() {
		return prepend(AbstractQueryExpression.or());
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public QueryBuilder not() {
		return append(AbstractQueryExpression.not());
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public QueryBuilder beginAnd() {
		return append(AbstractQueryExpression.and());
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public QueryBuilder beginOr() {
		return append(AbstractQueryExpression.or());
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public QueryBuilder end() {
		if (stack.size() > 1) {
			stack.pop();
		}
		return this;
	}
	
	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (!stack.isEmpty()) {
			return getRoot().toString();	
		} else {
			return "<empty-query>";
		}
	}
	
	// -----------------------------------------------------
	
	protected QueryExpression getRoot() {
		return stack.firstElement();
	}
	
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
	
}
