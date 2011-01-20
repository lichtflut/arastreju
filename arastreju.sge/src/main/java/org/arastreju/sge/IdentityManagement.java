/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.security.Credential;
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
	 * Register a new user.
	 * @param uniqueName The unique name.
	 * @param credential The credential.
	 * @return The user.
	 */
	User register(String uniqueName, Credential credential);
	
	/**
	 * Register a user for an existing Resource.
	 * @param uniqueName The unique name.
	 * @param credential The credential.
	 * @param corresponding The corresponding Resource.
	 * @return The user.
	 */
	User register(String uniqueName, Credential credential, ResourceNode corresponding);
	
	User login(String uniqueName, Credential credential);
	
	void addUserToRoles(User user, Role... roles);
	
	// -- ROLE MANAGEMENT ---------------------------------
	
	Role createRole(String name);
	
	Set<Role> getRoles();
	
	// -- PERMISSION MANAGEMENT ---------------------------
	
	Permission createPermission(String name);
	
	Set<Permission> getPermissions();

}
