/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge;

import java.util.Set;

import org.arastreju.sge.eh.ArastrejuException;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.security.Credential;
import org.arastreju.sge.security.LoginException;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;
import org.arastreju.sge.security.User;

/**
 * <p>
 *  Identity Management in Arastreju is based on data in a semantic graph.
 *  Each object of Identity Management - users, groups, roles, permissions -
 *  are represented by resources in the graph.
 * </p>
 *
 * <p>
 * 	Created Sep 1, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface IdentityManagement {
	
	/**
	 * Find the user identified by given identity.
	 * @param identity The identity.
	 * @return The attached user.
	 */
	User findUser(String identity);
	
	// -- LOGIN -------------------------------------------
	
	/**
	 * Login the user specified by name and credential.
	 * @param identifier The unique user name.
	 * @param credential The credential.
	 * @return The user if login was successful.
	 * @throws LoginException if the login was not successful.
	 */
	User login(String identifier, Credential credential) throws LoginException;
	
	// -- REGISTRATION ------------------------------------
	
	/**
	 * Register a new user.
	 * @param uniqueName The unique name.
	 * @param credential The credential.
	 * @return The user.
	 * @throws ArastrejuException 
	 */
	User register(String uniqueName, Credential credential) throws ArastrejuException;
	
	/**
	 * Register a user for an existing Resource. The Resource could be a person
	 * already known in the system. 
	 * @param uniqueName The unique name.
	 * @param credential The credential.
	 * @param corresponding The corresponding Resource.
	 * @return The user.
	 * @throws ArastrejuException 
	 */
	User register(String uniqueName, Credential credential, ResourceNode corresponding) throws ArastrejuException;
	
	/**
	 * Register an alternateID for a user.
	 * @param user An existing user.
	 * @param uniqueName Another unique name to identify the user.
	 * @return The user.
	 * @throws ArastrejuException 
	 */
	User registerAlternateID(User user, String uniqueName) throws ArastrejuException;
	
	/**
	 * Check if a user identifier is already in use.
	 * @param identifier The identifier to check.
	 * @return true if the name is in use.
	 */
	boolean isIdentifierInUse(String identifier);
	
	// -- ROLE MANAGEMENT ---------------------------------
	
	Role registerRole(String name);
	
	Set<Role> getRoles();
	
	void addUserToRoles(User user, Role... roles);
	
	void removeUserFromRoles(User user, Role... roles);
	
	/**
	 * Add permissions to a role.
	 * @param role The role. 
	 * @param permissions The permissions to be added.
	 */
	void addPermissionsToRole(Role role, Permission... permissions);
	
	// -- PERMISSION MANAGEMENT ---------------------------
	
	Permission registerPermission(String name);
	
	Set<Permission> getPermissions();

}
