/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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

}
