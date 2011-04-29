/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import static org.arastreju.sge.SNOPS.string;

import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
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
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.User#getEmail()
	 */
	public String getEmail() {
		return string(getIdentityNode().getSingleAssociationClient(Aras.HAS_EMAIL));
	}
	
	

}
