/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import java.util.Set;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;
import org.arastreju.sge.security.User;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ArastrejuRootUser implements User {

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Identity#getAssociatedResource()
	 */
	public ResourceNode getAssociatedResource() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Identity#getName()
	 */
	public String getName() {
		return Identity.ROOT;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Identity#getRoles()
	 */
	public Set<Role> getRoles() {
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Identity#getPermissions()
	 */
	public Set<Permission> getPermissions() {
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Identity#isInRole(org.arastreju.sge.security.Role)
	 */
	public boolean isInRole(Role role) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Identity#hasPermission(org.arastreju.sge.security.Permission)
	 */
	public boolean hasPermission(Permission permission) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.User#getEmail()
	 */
	public String getEmail() {
		return null;
	}

}
