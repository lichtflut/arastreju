/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.ogm.impl.enhance;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Interface implemented by all enhanced entity nodes.
 * </p>
 *
 * <p>
 * 	Created Jan 18, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public interface EnhancedEntityNode {

	ResourceNode aras$getResourceNode();
	
	void aras$setResourceNode(ResourceNode node);
	
	ResourceID aras$getID();
	
	

}
