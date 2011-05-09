/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import java.util.List;
import java.util.Set;

import org.arastreju.bindings.neo4j.impl.NeoDataStore;
import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.sge.IdentityManagement;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.security.Credential;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.LoginException;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;
import org.arastreju.sge.security.User;
import org.arastreju.sge.security.impl.ArastrejuRootUser;
import org.arastreju.sge.security.impl.UserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

/**
 * <p>
 *  Neo4J specific Identity Management. 
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoIdentityManagement implements IdentityManagement {
	
	private final ResourceIndex index;
	
	private final Logger logger = LoggerFactory.getLogger(NeoIdentityManagement.class);

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param store The neo store.
	 */
	public NeoIdentityManagement(final NeoDataStore store) {
		this.index = new ResourceIndex(store);
	}
	
	/**
	 * Constructor.
	 * @param index The resource index.
	 */
	public NeoIdentityManagement(final ResourceIndex index) {
		this.index = index;
	}
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public User login(final String name, final Credential credential) throws LoginException {
		logger.debug("trying to login user '" + name + "'.");
		
		final List<ResourceNode> found = index.lookup(Aras.IDENTIFIED_BY, name);
		if (found.size() > 1) {
			logger.error("More than on user with name '" + name + "' found.");
			throw new IllegalStateException("More than on user with name '" + name + "' found.");
		}
		if (found.isEmpty()){
			if (Identity.ROOT.equals(name)){
				return new ArastrejuRootUser();
			} else {
				throw new LoginException(ErrorCodes.LOGIN_USER_NOT_FOUND, "User does not exist: " + name);	
			}
		}
		
		final SNEntity user = found.get(0).asEntity();
		if (!credential.applies(user.getSingleAssociationClient(Aras.HAS_CREDENTIAL))){
			throw new LoginException(ErrorCodes.LOGIN_USER_CREDENTIAL_NOT_MATCH, "Wrong credential");
		}
		
		return new UserImpl(user);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#register(java.lang.String, org.arastreju.sge.security.Credential)
	 */
	public User register(String uniqueName, Credential credential) {
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#register(java.lang.String, org.arastreju.sge.security.Credential, org.arastreju.sge.model.nodes.ResourceNode)
	 */
	public User register(String uniqueName, Credential credential,
			ResourceNode corresponding) {
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#createRole(java.lang.String)
	 */
	public Role createRole(String name) {
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#getRoles()
	 */
	public Set<Role> getRoles() {
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#addUserToRoles(org.arastreju.sge.security.User, org.arastreju.sge.security.Role[])
	 */
	public void addUserToRoles(User user, Role... roles) {
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#createPermission(java.lang.String)
	 */
	public Permission createPermission(String name) {
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#getPermissions()
	 */
	public Set<Permission> getPermissions() {
		throw new NotYetImplementedException();
	}

}
