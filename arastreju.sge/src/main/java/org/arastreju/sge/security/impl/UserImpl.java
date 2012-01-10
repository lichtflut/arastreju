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
import static org.arastreju.sge.SNOPS.singleObject;
import static org.arastreju.sge.SNOPS.string;

import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.security.User;

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
public class UserImpl extends AbstractIdentity implements User {

	/**
	 * Constructor.
	 * @param userNode The node representing the user.
	 */
	public UserImpl(final ResourceNode userNode) {
		super(userNode);
		// trigger resolving of associations
		userNode.getAssociations();
	}
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public String getEmail() {
		return string(singleObject(getAssociatedResource(), Aras.HAS_EMAIL));
	}
	
	public void setEmail(String email) {
		assure(getAssociatedResource(), Aras.HAS_EMAIL, new SNText(email), Aras.IDENT);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public String getDomain() {
		return string(singleObject(getAssociatedResource(), Aras.BELONGS_TO_DOMAIN));
	}
	
	public void setDomain(String domain) {
		assure(getAssociatedResource(), Aras.BELONGS_TO_DOMAIN, new SNText(domain), Aras.IDENT);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName();
	}
	
}
