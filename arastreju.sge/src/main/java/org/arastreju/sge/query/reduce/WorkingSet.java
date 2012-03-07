/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query.reduce;

import java.util.Iterator;

import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Working set for select/reduce operations.
 * </p>
 *
 * <p>
 * 	Created Mar 6, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public interface WorkingSet {
	
	String getId();
	
	Iterator<ResourceNode> getNodes();
	

}
