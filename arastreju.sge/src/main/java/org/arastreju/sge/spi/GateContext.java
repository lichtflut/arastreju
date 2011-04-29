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
package org.arastreju.sge.spi;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.security.Credential;
import org.arastreju.sge.security.Identity;
import org.arastreju.sge.security.PasswordCredential;

/**
 * <p>
 *  Context for initialization of a new {@link ArastrejuGate}.
 * </p>
 *
 * <p>
 * 	Created Jan 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class GateContext {
	
	private String username;
	
	private Credential credential;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor for username/credential.
	 */
	public GateContext(final String username, final String credential) {
		this.username = username;
		this.credential = new PasswordCredential(credential);
	}
	
	/**
	 * Constructor for username/credential.
	 */
	public GateContext(final String username, final Credential credential) {
		this.username = username;
		this.credential = credential;
	}
	
	// -----------------------------------------------------

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the credential
	 */
	public Credential getCredential() {
		return credential;
	}

	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(final String credentials) {
		this.credential = new PasswordCredential(credentials);
	}
	
	// -----------------------------------------------------
	
	public boolean isRootContext() {
		return Identity.ROOT.equals(username);
	}
	
}
