/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security;

import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Represent a domain.
 * </p>
 *
 * <p>
 * 	Created Jan 11, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Domain {
	
	ResourceNode getAssociatedResource();
	
	String getUniqueName();
	
	String getTitle();
	
	String getDescription();

	boolean isDomesticDomain();
}
