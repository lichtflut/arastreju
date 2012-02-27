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

import static org.arastreju.sge.SNOPS.assure;

import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.security.User;

import de.lichtflut.infra.Infra;

/**
 * <p>
 *  Implementation of User.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class SNUser extends AbstractIdentity implements User {

	/**
	 * Constructor.
	 * @param userNode The node representing the user.
	 */
	public SNUser(final ResourceNode userNode) {
		super(userNode);
	}
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public String getEmail() {
		return stringValue(Aras.HAS_EMAIL);
	}
	
	public void setEmail(String email) {
		assure(this, Aras.HAS_EMAIL, new SNText(email), Aras.IDENT);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public String getDomain() {
		return stringValue(Aras.BELONGS_TO_DOMAIN);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Infra.coalesce(getName(), getEmail(), getQualifiedName().getSimpleName());
	}
	
}
