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
 *  Parameter for query nodes by their URI.
 * </p>
 *
 * <p>
 * 	Created Nov 8, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class UriParam implements QueryParam {

	private final String term;
	
	// -----------------------------------------------------
	
	/**
	 * @param term
	 */
	public UriParam(final String term) {
		this.term = term;
	}
	
	
	// -----------------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	public QueryOperator getOperator() {
		return QueryOperator.HAS_URI;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public String getName() {
		return "URI";
	}

	/** 
	 * {@inheritDoc}
	 */
	public Object getValue() {
		return term;
	}
	
	/** 
	* {@inheritDoc}
	*/
	@Override
	public String toString() {
		return "URI=" + term;
	}

}
