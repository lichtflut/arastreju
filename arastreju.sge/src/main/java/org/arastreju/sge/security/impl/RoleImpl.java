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

import java.util.Set;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.security.Permission;
import org.arastreju.sge.security.Role;

import de.lichtflut.infra.Infra;
import de.lichtflut.infra.exceptions.NotYetImplementedException;

/**
 * <p>
 * 	Implementation of a {@link Role}.
 * </p>
 *
 * <p>
 * 	Created Sep 2, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class RoleImpl implements Role {
	
	private final ResourceNode node;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param node The node representing this role.
	 */
	public RoleImpl(final ResourceNode node) {
		this.node = node;
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Role#getAssociatedResource()
	 */
	public ResourceNode getAssociatedResource() {
		return node;
	}
	

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Role#getName()
	 */
	public String getName() {
		return SNOPS.singleObject(node, Aras.HAS_UNIQUE_NAME)
			.asValue().getStringValue();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Role#getPermissions()
	 */
	public Set<Permission> getPermissions() {
		throw new NotYetImplementedException();
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return node.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Role) {
			final Role other = (Role) obj;
			return Infra.equals(node, other.getAssociatedResource());
		}
		return super.equals(obj);
	}

}
