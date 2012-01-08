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

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Query object.
 * </p>
 *
 * <p>
 * 	Created Nov 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Query {
	
	/**
	 * @param param
	 * @return This.
	 */
	Query add(QueryParam param);
	
	/**
	 * @param name
	 * @param value
	 * @return This.
	 */
	Query addField(String name, Object value);
	
	/**
	 * Constructor.
	 * @param name
	 * @param value
	 * @return This.
	 */
	Query addField(ResourceID name, Object value);
	
	/**
	 * @param name
	 * @param value
	 */
	Query addField(QualifiedName name, Object value);
	
	/**
	 * @param name
	 * @param value
	 */
	Query addURI(String term);
	
	/**
	 * @param name
	 * @param value
	 */
	Query addValue(String term);

	// ----------------------------------------------------
	
	/**
	 * @return
	 */
	Query and();

	/**
	 * @return
	 */
	Query or();

	/**
	 * @return
	 */
	Query not();

	/**
	 * @return
	 */
	Query beginAnd();

	/**
	 * @return
	 */
	Query beginOr();

	/**
	 * @return
	 */
	Query end();
	
	// ----------------------------------------------------
	
	/**
	 * Set criteria for sorting.
	 * @param sortCriteria The sort criteria.
	 * @return This.
	 */
	QueryBuilder setSortCriteria(SortCriteria sortCriteria);
	
	// -----------------------------------------------------

	QueryResult getResult();

	
}
