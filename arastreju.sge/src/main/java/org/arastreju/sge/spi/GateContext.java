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
package org.arastreju.sge.spi;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
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

	private final ArastrejuProfile profile;
	
	private String username;
	
	private Credential credential;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor for username/credential.
	 */
	public GateContext(final ArastrejuProfile profile) {
		this.profile = profile;
	}
	
	// -----------------------------------------------------

	/**
	 * @return the profile
	 */
	public ArastrejuProfile getProfile() {
		return profile;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public GateContext setUsername(String username) {
		this.username = username;
		return this;
	}

	/**
	 * @return the credential
	 */
	public Credential getCredential() {
		return credential;
	}

	/**
	 * @param credential the credentials to set
	 */
	public GateContext setCredential(final String credential) {
		setCredential(new PasswordCredential(credential));
		return this;
	}
	
	/**
	 * @param credential the credentials to set
	 */
	public GateContext setCredential(final Credential credential) {
		this.credential = credential;
		return this;
	}
	
	// -----------------------------------------------------
	
	public boolean isRootContext() {
		return Identity.ROOT.equals(username);
	}
	
}
