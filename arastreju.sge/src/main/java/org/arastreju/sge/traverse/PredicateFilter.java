/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.traverse;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.views.SNProperty;

/**
 * <p>
 *  Filter for statements based on predicates.
 * </p>
 *
 * <p>
 * 	Created Dec 22, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class PredicateFilter implements TraversalFilter {
	
	private final Set<ResourceID> allowed = new HashSet<ResourceID>();
	
	private final Set<ResourceID> follow = new HashSet<ResourceID>();
	
	// ----------------------------------------------------
	
	/**
	 * @param allowed
	 */
	public PredicateFilter(Collection<ResourceID> follow, Collection<ResourceID> allowed) {
		this.allowed.addAll(allowed);
		this.follow.addAll(follow);
	}
	
	/**
	 * Default constructor.
	 */
	public PredicateFilter() {
	}
	
	// ----------------------------------------------------
	
	public PredicateFilter addAllowed(ResourceID... allowed) {
		for (ResourceID current : allowed) {
			this.allowed.add(current);	
		}
		return this;
	}
	
	public PredicateFilter addFollow(ResourceID... follow) {
		for (ResourceID current : follow) {
			this.follow.add(current);	
		}
		return this;
	}
	
	// ----------------------------------------------------

	/** 
	* {@inheritDoc}
	*/
	@Override
	public TraverseCommand accept(Statement stmt) {
		final SNProperty predicate = stmt.getPredicate().asResource().asProperty();
		for (ResourceID current : follow) {
			if (predicate.isSubPropertyOf(current)) {
				return TraverseCommand.ACCEPPT_CONTINUE;
			}
		}
		for (ResourceID current : allowed) {
			if (predicate.isSubPropertyOf(current)) {
				return TraverseCommand.ACCEPT;
			}
		}
		return TraverseCommand.STOP;
	}

}
