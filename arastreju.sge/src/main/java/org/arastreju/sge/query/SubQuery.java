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
package org.arastreju.sge.query;

/**
 * <p>
 *  A sub query, that will be included literally as is into the outer query.
 * </p>
 *
 * <p>
 * 	Created Jan 20, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class SubQuery implements QueryParam {

	private final String query;
	
	// ----------------------------------------------------
	
	/**
	 * @param query
	 */
	public SubQuery(final String query) {
		this.query = query;
	}
	
	// ----------------------------------------------------

	/** 
	 * {@inheritDoc}
	 */
	public QueryOperator getOperator() {
		return QueryOperator.SUB_QUERY;
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getName() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	public Object getValue() {
		return query;
	}
	
	// ----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return query;
	}

}
