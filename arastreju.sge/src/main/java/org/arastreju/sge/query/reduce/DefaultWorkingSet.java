/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
