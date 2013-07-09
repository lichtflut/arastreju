/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.query.exp;

import org.arastreju.sge.query.QueryExpression;
import org.arastreju.sge.query.QueryOperator;

import java.util.ArrayList;
import java.util.List;

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
	
	// -----------------------------------------------------

    @Override
	public boolean isLeaf() {
		return false;
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
		sb.append("( ");
		boolean first = true;
		for (QueryExpression child : children) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(child);
		}
		sb.append(" )");
		return sb.toString();
	}

}