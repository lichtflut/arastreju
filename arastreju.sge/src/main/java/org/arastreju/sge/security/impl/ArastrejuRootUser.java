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
import java.util.Set;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;
import org.arastreju.sge.security.User;
import org.arastreju.sge.spi.GateContext;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

/**
 * <p>
 *  User representing root.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ArastrejuRootUser implements User, Serializable {

	private String domain = GateContext.MASTER_DOMAIN;
	
	// ----------------------------------------------------
	
	/**
	 * Default constructor for default domain.
	 */
	public ArastrejuRootUser() {
	}
	
	/**
	 * @param domain
	 */
	public ArastrejuRootUser(String domain) {
		this.domain = domain;
	}
	
	// ----------------------------------------------------
	
	
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
		return Identity.ROOT;
	}

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
	public boolean isInRole(String rolename) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasPermission(String permission) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getEmail() {
		return "root@system";
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public String getDomain() {
		return domain;
	}

}
