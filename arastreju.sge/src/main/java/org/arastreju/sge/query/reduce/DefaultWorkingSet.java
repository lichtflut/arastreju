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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Mar 6, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class DefaultWorkingSet implements WorkingSet {

	private final String id;
	
	private final Set<ResourceNode> nodes = new HashSet<ResourceNode>();  
	
	// ----------------------------------------------------
	

	/**
	 * Constructor.
	 * @param id The working set ID.
	 */
	public DefaultWorkingSet(String id) {
		this.id = id;
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
		return nodes.iterator();
	}

}
