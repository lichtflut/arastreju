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
package org.arastreju.sge.query.reduce;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;

import java.util.Iterator;

/**
 * <p>
 *  Working Set based on a query.
 * </p>
 *
 * <p>
 * 	Created Mar 6, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class QueryBasedWorkingSet implements WorkingSet {

	private final String id;
	
	private QueryResult result;
	
	private Query query;
	
	// ----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param id The working set ID.
	 */
	public QueryBasedWorkingSet(String id, Query query) {
		this.id = id;
		this.query = query;
	}
	
	/**
	 * Constructor.
	 * @param id The working set ID.
	 */
	public QueryBasedWorkingSet(String id, QueryResult result) {
		this.id = id;
		this.result = result;
	}
	
	
	// ----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}


	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<ResourceNode> getNodes() {
		return getResult().iterator();
	}
	
	// ----------------------------------------------------
	
	private QueryResult getResult() {
		if (result == null) {
			result = query.getResult();
		}
		return result;
	}

}
