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

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *  Thread safe context for select/reduce operations.
 * </p>
 *
 * <p>
 * 	Created Mar 6, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class ReduceContext {
	
	private final ConcurrentHashMap<String, WorkingSet> map = new ConcurrentHashMap<String, WorkingSet>();
	
	// ----------------------------------------------------
	
	/**
	 * Get the working set.
	 * @param id
	 * @return
	 */
	public final WorkingSet getWorkingSet(final String id) {
		WorkingSet workingSet = map.get(id);
		if (workingSet == null) {
			workingSet = map.putIfAbsent(id, new DefaultWorkingSet(id));
		}
		return workingSet;
	}
	
	/**
	 * Register this working set.
	 * @param set The set to register.
	 */
	public void register(final WorkingSet set) {
		map.put(set.getId(), set);
	}

}
