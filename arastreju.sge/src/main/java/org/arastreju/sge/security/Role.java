/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security;

import java.util.Set;

import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jan 5, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Role {
	
	ResourceNode getAssociatedResource();
	
	String getName();

	Set<Permission> getPermissions();
	
}