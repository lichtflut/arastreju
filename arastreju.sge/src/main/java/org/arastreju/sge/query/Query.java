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
 *  Query object to dynamically create a complex query and retrieve it's result.
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
	 * Add a common query param.
	 * @param param The param.
	 * @return This.
	 */
	Query add(QueryParam param);
	
	/**
	 * Add a param querying a field, i.e. a property.
	 * @param name The name.
	 * @param value The value.
	 * @return This.
	 */
	Query addField(String name, Object value);
	
	/**
	 * Add a param querying a field, i.e. a property.
	 * @param name The name.
	 * @param value The value.
	 * @return This.
	 */
	Query addField(ResourceID name, Object value);
	
	/**
	 * Add a param querying a field, i.e. a property.
	 * @param name The name.
	 * @param value The value.
	 * @return This.
	 */
	Query addField(QualifiedName name, Object value);
	
	/**
	 * Add a parameter for the URI.
	 * @param term The term to match the URI. 
	 * @return This.
	 */
	Query addURI(String term);
	
	/**
	 * Add a parameter for a (literal) value query.
	 * @param term The term.
	 */
	Query addValue(String term);
	
	/**
	 * Add a parameter for a related resource.
	 * @param name The name.
	 * @param term The term.
	 */
	Query addRelation(String term);

	// ----------------------------------------------------
	
	/**
	 * Connect the previous and the next parameter with AND. 
	 * @return This.
	 */
	Query and();

	/**
	 * Connect the previous and the next parameter with OR. 
	 * @return This.
	 */
	Query or();

	/**
	 * Invert the next parameter or expression.
	 * @return This.
	 */
	Query not();

	/**
	 * Begin a block in which all parameters are AND-conjuncted. 
	 * @return This
	 */
	Query beginAnd();

	/**
	 * Begin a block in which all parameters are OR-conjuncted.
	 * @return This.
	 */
	Query beginOr();

	/**
	 * Close the block that has been opened with beginAnd() or beginOr().
	 * @return This.
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

	/**
	 * Execute the query and retrieve the result.
	 * @return The query result.
	 */
	QueryResult getResult();
	
}
