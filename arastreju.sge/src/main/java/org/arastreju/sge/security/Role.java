/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.security;

import java.util.Set;

import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Representation of a user's role.
 * </p>
 *
 * <p>
 * 	Created Jan 5, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Role {
	
	/**
	 * Get the resource node associated with this role.
	 * @return The corresponding resource note.
	 */ 
	ResourceNode getAssociatedResource();
	
	/**
	 * Get the unique role name.
	 * @return The name.
	 */
	String getName();

	/**
	 * Get the permissions assigned to this role.
	 * @return The role's permissions.
	 */
	Set<Permission> getPermissions();
	
}
