/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.traverse;

import org.arastreju.sge.model.Statement;

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
public class AcceptAllFilter implements TraversalFilter {
	
	/**
	 * Default constructor.
	 */
	public AcceptAllFilter() {
	}
	
	// ----------------------------------------------------
	
	/** 
	* {@inheritDoc}
	*/
	@Override
	public TraverseCommand accept(Statement stmt) {
		return TraverseCommand.ACCEPPT_CONTINUE;
	}

}
