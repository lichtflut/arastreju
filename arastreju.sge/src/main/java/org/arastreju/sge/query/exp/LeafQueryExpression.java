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