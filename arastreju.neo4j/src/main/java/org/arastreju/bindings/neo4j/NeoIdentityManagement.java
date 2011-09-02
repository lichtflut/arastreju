/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.sge.IdentityManagement;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.CTX;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.security.Credential;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.LoginException;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;
import org.arastreju.sge.security.User;
import org.arastreju.sge.security.impl.ArastrejuRootUser;
import org.arastreju.sge.security.impl.PermissionImpl;
import org.arastreju.sge.security.impl.RoleImpl;
import org.arastreju.sge.security.impl.UserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lichtflut.infra.Infra;

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
	
	private final Logger logger = LoggerFactory.getLogger(NeoIdentityManagement.class);
	
	private final ResourceIndex index;
	
	private final SemanticNetworkAccess store;

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param store The neo store.
	 */
	public NeoIdentityManagement(final SemanticNetworkAccess store) {
		this.store = store;
		this.index = new ResourceIndex(store);
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

	/**
	 * {@inheritDoc}
	 */
	public User register(final String uniqueName, final Credential credential) {
		return register(uniqueName, credential, new SNEntity());
	}

	/**
	 * {@inheritDoc}
	 */
	public User register(final String name, final Credential credential, final ResourceNode corresponding) {
		assertUnique(Aras.USER, name);
		SNOPS.associate(corresponding, Aras.IDENTIFIED_BY, new SNText(name), CTX.IDENT);
		SNOPS.associate(corresponding, Aras.HAS_CREDENTIAL, new SNText(credential.stringRepesentation()), CTX.IDENT);
		SNOPS.associate(corresponding, RDF.TYPE, Aras.USER, CTX.IDENT);
		store.attach(corresponding);
		return new UserImpl(corresponding);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#createRole(java.lang.String)
	 */
	public Role createRole(final String name) {
		assertUnique(Aras.ROLE, name);
		final SNResource role = new SNResource();
		SNOPS.associate(role, Aras.HAS_UNIQUE_NAME, new SNText(name), CTX.IDENT);
		SNOPS.associate(role, RDF.TYPE, Aras.ROLE, CTX.IDENT);
		store.attach(role);
		return new RoleImpl(role);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#getRoles()
	 */
	public Set<Role> getRoles() {
		final List<ResourceNode> nodes = index.lookup(RDF.TYPE, Aras.ROLE);
		final Set<Role> roles = new HashSet<Role>(nodes.size());
		for(ResourceNode current: nodes) {
			roles.add(new RoleImpl(current));
		}
		return roles;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#addUserToRoles(org.arastreju.sge.security.User, org.arastreju.sge.security.Role[])
	 */
	public void addUserToRoles(final User user, final Role... roles) {
		final ResourceNode userNode = user.getAssociatedResource();
		store.attach(userNode);
		for (Role role : roles) {
			SNOPS.associate(userNode, Aras.HAS_ROLE, role.getAssociatedResource(), CTX.IDENT);
		}
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#createPermission(java.lang.String)
	 */
	public Permission createPermission(final String name) {
		assertUnique(Aras.PERMISSION, name);
		final SNResource permission = new SNResource();
		SNOPS.associate(permission, Aras.HAS_UNIQUE_NAME, new SNText(name), CTX.IDENT);
		SNOPS.associate(permission, RDF.TYPE, Aras.PERMISSION, CTX.IDENT);
		store.attach(permission);
		return new PermissionImpl(permission);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.IdentityManagement#getPermissions()
	 */
	public Set<Permission> getPermissions() {
		final List<ResourceNode> nodes = index.lookup(RDF.TYPE, Aras.PERMISSION);
		final Set<Permission> permissions = new HashSet<Permission>(nodes.size());
		for(ResourceNode current: nodes) {
			permissions.add(new PermissionImpl(current));
		}
		return permissions;
	}
	
	// -----------------------------------------------------
	
	/**
	 * TODO: improve loolup.
	 */
	private void assertUnique(final ResourceID type, final String name) {
		final List<ResourceNode> all = index.lookup(RDF.TYPE, type);
		for (ResourceNode current : all) {
			final SemanticNode currentName = SNOPS.singleObject(current, Aras.HAS_UNIQUE_NAME);
			if (Infra.equals(name, SNOPS.string(currentName))) {
				throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE, 
						"Name already used for type " + type + ": " + name);
			}
		}
	}

}
