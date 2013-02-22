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
package org.arastreju.sge.traverse;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;

/**
 * <p>
 *  Filter based on disallowed predicates.
 * </p>
 *
 * <p>
 * 	Created Dec 22, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NotPredicateFilter implements TraversalFilter {
	
	private final Set<ResourceID> disallowed = new HashSet<ResourceID>();
	
	// ----------------------------------------------------
	
	/**
	 * @param disallowed
	 */
	public NotPredicateFilter(Collection<ResourceID> disallowed) {
		this.disallowed.addAll(disallowed);
	}
	
	/**
	 * @param disallowed
	 */
	public NotPredicateFilter(ResourceID... disallowed) {
		for (ResourceID current : disallowed) {
			this.disallowed.add(current);	
		}
	}
	
	// ----------------------------------------------------

	/** 
	* {@inheritDoc}
	*/
	@Override
	public TraverseCommand accept(Statement stmt) {
		if (disallowed.contains(stmt.getPredicate())) {
			return TraverseCommand.STOP;
		} else {
			return TraverseCommand.ACCEPPT_CONTINUE;
		}
	}

}
