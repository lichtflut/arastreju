/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import java.util.Collections;
import java.util.Set;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;
import org.arastreju.sge.security.User;

/**
 * <p>
 *  Special user class for unidentified users.
 * </p>
 *
 * <p>
 * 	Created Nov 28, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class AnonymousUser implements User {

	/** 
	 * {@inheritDoc}
	 */
	public ResourceNode getAssociatedResource() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getName() {
		return Identity.ANONYMOUS;
	}

	/** 
	 * {@inheritDoc}
	 */
	public Set<Role> getRoles() {
		return Collections.emptySet();
	}

	/** 
	 * {@inheritDoc}
	 */
	public Set<Permission> getPermissions() {
		return Collections.emptySet();
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean isInRole(String rolename) {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean hasPermission(String permission) {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getEmail() {
		return null;
	}

}
