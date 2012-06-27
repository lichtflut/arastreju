/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
