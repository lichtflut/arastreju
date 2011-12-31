/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import java.io.Serializable;
import java.util.Set;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;
import org.arastreju.sge.security.User;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

/**
 * <p>
 *  User representing root.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ArastrejuRootUser implements User, Serializable {

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
		return Identity.ROOT;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Role> getRoles() {
		throw new NotYetImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Permission> getPermissions() {
		throw new NotYetImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInRole(String rolename) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasPermission(String permission) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getEmail() {
		return "root@system";
	}

}
