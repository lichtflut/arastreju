/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.traverse;

import java.util.Collection;
import java.util.Collections;
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
     * Constructor.
     * @param follow The predicates to follow.
	 * @param allowed The allowed predicates.
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
        Collections.addAll(this.allowed, allowed);
		return this;
	}
	
	public PredicateFilter addFollow(ResourceID... follow) {
        Collections.addAll(this.follow, follow);
		return this;
	}
	
	// ----------------------------------------------------

	@Override
	public TraverseCommand accept(Statement stmt) {
		final SNProperty predicate = SNProperty.from(stmt.getPredicate());
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
