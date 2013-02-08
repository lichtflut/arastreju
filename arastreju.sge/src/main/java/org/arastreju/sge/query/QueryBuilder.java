/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.query;

import java.util.Stack;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
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
	
	private SortCriteria criteria;

	// -----------------------------------------------------
	
	/**
	 * Default constructor.
	 */
	public QueryBuilder() {}
	
	// -----------------------------------------------------

    /**
     * Get the single node from the query result.
     * @return A single node.
     */
    public ResourceNode getSingleNode() {
        return getResult().getSingleNode();
    }

    // ----------------------------------------------------
	
	@Override
	public QueryBuilder add(final QueryParam param) {
		return append(param);
	}

    @Override
	public Query addField(String name, Object value) {
		return append(new FieldParam(name, value));
	}

    @Override
	public Query addField(ResourceID name, Object value) {
		return append(new FieldParam(name, value));
	}

    @Override
	public Query addField(QualifiedName name, Object value) {
		return append(new FieldParam(name, value));
	}

    @Override
	public Query addURI(String term) {
		return append(new UriParam(term));
	}

    @Override
	public Query addValue(String term) {
		return append(new ValueParam(term));
	}

    @Override
	public Query addRelation(String term) {
		return append(new RelationQuery(term));
	}

    @Override
	public QueryBuilder and() {
		return prepend(AbstractQueryExpression.and());
	}

    @Override
	public QueryBuilder or() {
		return prepend(AbstractQueryExpression.or());
	}

    @Override
	public QueryBuilder not() {
		return append(AbstractQueryExpression.not());
	}

    @Override
	public QueryBuilder beginAnd() {
		return append(AbstractQueryExpression.and());
	}

    @Override
	public QueryBuilder beginOr() {
		return append(AbstractQueryExpression.or());
	}
	
	@Override
	public QueryBuilder end() {
		if (stack.size() > 1) {
			stack.pop();
		}
		return this;
	}
	
	// ----------------------------------------------------
	
	public QueryBuilder setSortCriteria(final SortCriteria sortCriteria) {
		this.criteria = sortCriteria;
		return this;
	}
	
	// -----------------------------------------------------

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
	
	protected SortCriteria getSortCriteria() {
		return criteria;
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
