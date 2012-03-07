/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
