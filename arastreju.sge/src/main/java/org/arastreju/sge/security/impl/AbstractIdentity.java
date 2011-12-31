/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;

import de.lichtflut.infra.Infra;

/**
 * <p>
 *  Abstract implementation of an Identity.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractIdentity implements Identity, Serializable {

	private final ResourceNode identityNode;

	// -----------------------------------------------------
	
	/**
	 * @param identityNode
	 */
	public AbstractIdentity(final ResourceNode identityNode) {
		this.identityNode = identityNode;
		// trigger getName() check
		getName();
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode getAssociatedResource() {
		return identityNode;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		final SemanticNode idNode = SNOPS.singleObject(identityNode, Aras.HAS_UNIQUE_NAME);
		if (idNode == null) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE, "Identity has no name!");
		}
		return idNode.asValue().getStringValue();
	}
	
	// ----------------------------------------------------- 

	/**
	 * {@inheritDoc}
	 */
	public Set<Role> getRoles() {
		final Set<SemanticNode> roleNodes = SNOPS.objects(identityNode, Aras.HAS_ROLE);
		final Set<Role> roles = new HashSet<Role>(roleNodes.size());
		for (SemanticNode node : roleNodes) {
			roles.add(new RoleImpl(node.asResource()));
		}
		return roles;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Permission> getPermissions() {
		final Set<SemanticNode> permissionNodes = SNOPS.objects(identityNode, Aras.HAS_PERMISSION);
		final Set<Permission> permissions = new HashSet<Permission>(permissionNodes.size());
		for (SemanticNode node : permissionNodes) {
			permissions.add(new PermissionImpl(node.asResource()));
		}
		return permissions;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInRole(String role) {
		for(Role current : getRoles()) {
			if (current.getName().equals(role)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasPermission(String permission) {
		for(Permission current : getPermissions()) {
			if (current.getName().equals(permission)) {
				return true;
			}
		}
		return false;
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return identityNode.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Identity) {
			final Identity other = (Identity) obj;
			return Infra.equals(identityNode, other.getAssociatedResource());
		}
		return super.equals(obj);
	}
	
}
