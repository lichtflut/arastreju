/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import java.io.Serializable;
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
import de.lichtflut.infra.exceptions.NotYetImplementedException;

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
	public boolean isInRole(Role role) {
		throw new NotYetImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasPermission(Permission permission) {
		throw new NotYetImplementedException();
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
