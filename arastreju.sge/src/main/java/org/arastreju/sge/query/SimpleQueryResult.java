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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Result of a {@link Query}.
 * </p>
 *
 * <p>
 * 	Created Nov 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class SimpleQueryResult implements QueryResult {
	
	public static QueryResult EMPTY = new SimpleQueryResult(Collections.<ResourceNode>emptyList());

	private List<ResourceNode> list;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor based on a list.
	 */
	public SimpleQueryResult(final List<ResourceNode> list) {
		this.list = list;
	}
	
	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public int size() {
		return list.size();
	};
	
	/** 
	 * {@inheritDoc}
	 */
	public List<ResourceNode> toList() {
		return list;
	}
	
	/** 
	* {@inheritDoc}
	*/
	public List<ResourceNode> toList(int max) {
		if (list.size() > max) {
			return list.subList(0, max -1);
		}
		return list;
	}
	
	/** 
	* {@inheritDoc}
	*/
	public List<ResourceNode> toList(int offset, int max) {
		if (offset >= list.size()) {
			return Collections.emptyList();
		}
		if (max + offset > list.size()) {
			return list.subList(offset, list.size() -1);
		} else  {
			return list.subList(offset, offset + max -1);	
		}
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public Iterator<ResourceNode> iterator() {
		return list.iterator();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	/** 
	* {@inheritDoc}
	*/
	public ResourceNode getSingleNode() {
		if (list.isEmpty()) {
			return null;
		} else if (list.size() > 1) {
			throw new IllegalStateException("More than one result found.");
		} else {
			return list.get(0);
		}
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void close() {
		// do nothing.
	}
	
}
