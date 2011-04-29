/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import java.util.Set;

import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;

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
public abstract class AbstractIdentity implements Identity {

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
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Identity#getAssociatedResource()
	 */
	public ResourceNode getAssociatedResource() {
		return identityNode;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Identity#getName()
	 */
	public String getName() {
		final SemanticNode idNode = identityNode.getSingleAssociationClient(Aras.IDENTIFIED_BY);
		if (idNode == null) {
			throw new IllegalStateException("Identity has no name!");
		}
		return idNode.asValue().getStringValue();
	}
	
	// ----------------------------------------------------- 

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
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Identity#hasPermission(org.arastreju.sge.security.Permission)
	 */
	public boolean hasPermission(Permission permission) {
		throw new NotYetImplementedException();
	}
	
	// -----------------------------------------------------
	
	/**
	 * @return the identityNode
	 */
	public ResourceNode getIdentityNode() {
		return identityNode;
	}
	
	
}
