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

import java.util.Arrays;

/**
 * <p>
 *  Criteria for sorting of queries.
 * </p>
 *
 * <p>
 * 	Created Jan 3, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class SortCriteria {
	
	private final String[] columns;
	
	// ----------------------------------------------------
	
	/**
	 * Constructor.
	 */
	public SortCriteria(final String... columns) {
		if (columns == null) {
			throw new IllegalArgumentException("columns may not be null.");
		}
		this.columns = columns;
	}
	
	// ----------------------------------------------------
	
	/**
	 * @return the columns
	 */
	public String[] getColumns() {
		return columns;
	}
	
	// ----------------------------------------------------
	
	@Override
	public String toString() {
		return Arrays.toString(columns);
	}

}
