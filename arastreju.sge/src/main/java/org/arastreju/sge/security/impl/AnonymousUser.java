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
package org.arastreju.sge.security.impl;

import java.util.Collections;
import java.util.Set;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;
import org.arastreju.sge.security.User;

/**
 * <p>
 *  Special user class for unidentified users.
 * </p>
 *
 * <p>
 * 	Created Nov 28, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class AnonymousUser implements User {

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
		return Identity.ANONYMOUS;
	}

	/** 
	 * {@inheritDoc}
	 */
	public Set<Role> getRoles() {
		return Collections.emptySet();
	}

	/** 
	 * {@inheritDoc}
	 */
	public Set<Permission> getPermissions() {
		return Collections.emptySet();
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean isInRole(String rolename) {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean hasPermission(String permission) {
		return false;
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getEmail() {
		return null;
	}

}
