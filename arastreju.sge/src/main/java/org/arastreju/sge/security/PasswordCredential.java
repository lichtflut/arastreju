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
package org.arastreju.sge.security;

import org.arastreju.sge.model.nodes.SemanticNode;

import de.lichtflut.infra.Infra;

/**
 * <p>
 *  The standard credential, just a string.
 * </p>
 *
 * <p>
 * 	Created Jan 20, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class PasswordCredential implements Credential {
	
	private String password;
	
	// -----------------------------------------------------
	
	public PasswordCredential(final String password) {
		this.password = password;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean applies(final SemanticNode node) {
		return node != null && node.isValueNode() && 
			Infra.equals(node.asValue().getStringValue(), password);
	}
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public String stringRepesentation() {
		return password;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return password == null || password.length() == 0;
	}

}
