/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import static org.arastreju.sge.SNOPS.associate;
import static org.arastreju.sge.SNOPS.singleObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.bindings.neo4j.query.NeoQueryBuilder;
import org.arastreju.sge.IdentityManagement;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.eh.ArastrejuException;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;
import org.arastreju.sge.security.Credential;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.LoginException;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;
import org.arastreju.sge.security.User;
import org.arastreju.sge.security.impl.AnonymousUser;
import org.arastreju.sge.security.impl.ArastrejuRootUser;
import org.arastreju.sge.security.impl.PermissionImpl;
import org.arastreju.sge.security.impl.RoleImpl;
import org.arastreju.sge.security.impl.UserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		this.index = store.getIndex();
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public User findUser(final String identity) {
		final QueryResult result = index.lookup(Aras.IDENTIFIED_BY, identity);
		if (result.size() > 1) {
			logger.error("More than on user with name '" + identity + "' found.");
			throw new IllegalStateException("More than on user with name '" + identity + "' found.");
		} else if (result.isEmpty()) {
			return null;
		} else {
			return new UserImpl(result.getSingleNode());
		}
	};

	/**
	 * {@inheritDoc}
	 */
	public User login(final String name, final Credential credential) throws LoginException {
		
		logger.debug("trying to login user '" + name + "'.");
		if (name == null) {
			throw new LoginException(ErrorCodes.LOGIN_INVALID_DATA, "No username given");	
		}
		final QueryResult found = index.lookup(Aras.IDENTIFIED_BY, name);
		if (found.size() > 1) {
			logger.error("More than on user with name '" + name + "' found.");
			throw new IllegalStateException("More than on user with name '" + name + "' found.");
		}
		if (found.isEmpty()){
			if (Identity.ROOT.equals(name)){
				return new ArastrejuRootUser();
			} else if (Identity.ANONYMOUS.equals(name)) {
				return new AnonymousUser();
			} else {
				throw new LoginException(ErrorCodes.LOGIN_USER_NOT_FOUND, "User does not exist: " + name);	
			}
		}
		
		final SNEntity user = found.getSingleNode().asEntity();
		if (!credential.applies(singleObject(user, Aras.HAS_CREDENTIAL))){
			throw new LoginException(ErrorCodes.LOGIN_USER_CREDENTIAL_NOT_MATCH, "Wrong credential");
		}
		
		return new UserImpl(user);
	}

	/**
	 * {@inheritDoc}
	 */
	public User register(final String uniqueName, final Credential credential) throws ArastrejuException {
		return register(uniqueName, credential, new SNEntity());
	}

	/**
	 * {@inheritDoc}
	 */
	public User register(final String name, final Credential credential, final ResourceNode corresponding) throws ArastrejuException {
		assertUniqueIdentity(name);
		associate(corresponding, Aras.HAS_UNIQUE_NAME, new SNText(name), Aras.IDENT);
		associate(corresponding, Aras.IDENTIFIED_BY, new SNText(name), Aras.IDENT);
		associate(corresponding, Aras.HAS_CREDENTIAL, new SNText(credential.stringRepesentation()), Aras.IDENT);
		associate(corresponding, RDF.TYPE, Aras.USER, Aras.IDENT);
		store.attach(corresponding);
		return new UserImpl(corresponding);
	}
	
	/** 
	* {@inheritDoc}
	*/
	public User registerAlternateID(User user, String uniqueName) throws ArastrejuException {
		assertUniqueIdentity(uniqueName);
		final ResourceNode node = store.resolve(user.getAssociatedResource());
		associate(node, Aras.IDENTIFIED_BY, new SNText(uniqueName), Aras.IDENT);
		return user;
	}
	
	// ----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public Role registerRole(final String name) {
		final ResourceNode existing = findItem(Aras.ROLE, name);
		if (existing != null) {
			return new RoleImpl(existing);
		}
		final SNResource role = new SNResource();
		associate(role, Aras.HAS_UNIQUE_NAME, new SNText(name), Aras.IDENT);
		associate(role, RDF.TYPE, Aras.ROLE, Aras.IDENT);
		store.attach(role);
		return new RoleImpl(role);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Role> getRoles() {
		final List<ResourceNode> nodes = index.lookup(RDF.TYPE, Aras.ROLE).toList();
		final Set<Role> roles = new HashSet<Role>(nodes.size());
		for(ResourceNode current: nodes) {
			roles.add(new RoleImpl(current));
		}
		return roles;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addUserToRoles(final User user, final Role... roles) {
		final ResourceNode userNode = user.getAssociatedResource();
		store.attach(userNode);
		for (Role role : roles) {
			associate(userNode, Aras.HAS_ROLE, role.getAssociatedResource(), Aras.IDENT);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addPermissionsToRole(final Role role, final Permission... permissions) {
		final ResourceNode roleNode = role.getAssociatedResource();
		store.attach(roleNode);
		for (Permission permission : permissions) {
			associate(roleNode, Aras.CONTAINS, permission.getAssociatedResource(), Aras.IDENT);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Permission registerPermission(final String name) {
		final ResourceNode existing = findItem(Aras.PERMISSION, name);
		if (existing != null) {
			return new PermissionImpl(existing);
		}
		final SNResource permission = new SNResource();
		associate(permission, Aras.HAS_UNIQUE_NAME, new SNText(name), Aras.IDENT);
		associate(permission, RDF.TYPE, Aras.PERMISSION, Aras.IDENT);
		store.attach(permission);
		return new PermissionImpl(permission);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Permission> getPermissions() {
		final List<ResourceNode> nodes = index.lookup(RDF.TYPE, Aras.PERMISSION).toList();
		final Set<Permission> permissions = new HashSet<Permission>(nodes.size());
		for(ResourceNode current: nodes) {
			permissions.add(new PermissionImpl(current));
		}
		return permissions;
	}
	
	// -----------------------------------------------------
	
	private ResourceNode findItem(final ResourceID type, final String name) {
		final Query query = new NeoQueryBuilder(index);
		query.addField(RDF.TYPE, type);
		query.and();
		query.addField(Aras.HAS_UNIQUE_NAME, name);
		final QueryResult result = query.getResult();
		if (result.size() > 1) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE, 
					"Unique name is not unique for " + type + ": " + name);
		} else {
			return result.getSingleNode();
		}
	}
	
	protected void assertUniqueIdentity(final String name) throws ArastrejuException {
		final QueryResult found = index.lookup(Aras.IDENTIFIED_BY, name);
		if (found.size() > 0) {
			logger.error("More than on user with name '" + name + "' found.");
			throw new ArastrejuException(ErrorCodes.REGISTRATION_NAME_ALREADY_IN_USE, 
					"More than on user with name '" + name + "' found.");
		}
	}
	
}
