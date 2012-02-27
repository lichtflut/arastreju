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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.arastreju.sge.SNOPS.*;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.ResourceView;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;

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
public abstract class AbstractIdentity extends ResourceView implements Identity, Serializable {

	/**
	 * @param identityNode
	 */
	public AbstractIdentity(final ResourceNode identityNode) {
		super(identityNode);
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode getAssociatedResource() {
		return getResource();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return string(singleObject(getResource(), Aras.HAS_UNIQUE_NAME));
	}
	
	// ----------------------------------------------------- 

	/**
	 * {@inheritDoc}
	 */
	public Set<Role> getRoles() {
		final Set<SemanticNode> roleNodes = objects(getResource(), Aras.HAS_ROLE);
		final Set<Role> roles = new HashSet<Role>(roleNodes.size());
		for (SemanticNode node : roleNodes) {
			roles.add(new SNRole(node.asResource()));
		}
		return roles;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Permission> getPermissions() {
		final Set<Permission> permissions = new HashSet<Permission>();
		for(Role role : getRoles()) {
			final Set<SemanticNode> permissionNodes = objects(role, Aras.CONTAINS);
			for (SemanticNode node : permissionNodes) {
				permissions.add(new SNPermission(node.asResource()));
			}
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
	
}
