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
	
	public static LeafQueryExpression leaf(final QueryParam param) {
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
	}

}
