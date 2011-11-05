/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query.exp;

import java.util.ArrayList;
import java.util.List;

import org.arastreju.sge.query.QueryExpression;
import org.arastreju.sge.query.QueryOperator;

class GroupedQueryExpression extends AbstractQueryExpression {

	private final List<QueryExpression> children = new ArrayList<QueryExpression>();

	public GroupedQueryExpression(final QueryOperator queryOperator, final List<AbstractQueryExpression> children) {
		super(queryOperator);
		this.children.addAll(children);
	}

	public GroupedQueryExpression(final QueryOperator queryOperator, final AbstractQueryExpression expr) {
		super(queryOperator);
		this.children.add(expr);
	}
	
	public GroupedQueryExpression(final QueryOperator queryOperator) {
		super(queryOperator);
	}

	@Override
	public List<QueryExpression> getChildren() {
		return children;
	}
	
	@Override
	public void add(final QueryExpression expr) {
		children.add(expr);
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getOperator().name());
		sb.append("(");
		boolean first = true;
		for (QueryExpression child : children) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append(child);
		}
		sb.append(")");
		return sb.toString();
	}

}