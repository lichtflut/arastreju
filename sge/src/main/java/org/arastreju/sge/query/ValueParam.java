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
 *  Parameter for query nodes by one of their values, whatever field this is.
 * </p>
 *
 * <p>
 * 	Created Nov 8, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ValueParam implements QueryParam {

	private final String term;
	
	// -----------------------------------------------------
	
	/**
     * Constructor.
	 * @param term The term.
	 */
	public ValueParam(final String term) {
		this.term = term;
	}
	
	// -----------------------------------------------------

    @Override
	public QueryOperator getOperator() {
		return QueryOperator.HAS_VALUE;
	}

    @Override
	public String getName() {
		return "VAL";
	}

    @Override
	public Object getValue() {
		return term;
	}
	
	// -----------------------------------------------------
	
	@Override
	public String toString() {
		return "VAL='" + term + "'";
	}

}
