/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import junit.framework.Assert;

import org.arastreju.bindings.neo4j.impl.NeoDataStore;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.security.LoginException;
import org.arastreju.sge.security.PasswordCredential;
import org.arastreju.sge.security.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eh.ErrorCodes;

/**
 * <p>
 *  Testcase for {@link NeoIdentityManagement}.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoIdentityManagementTest {

	private NeoDataStore store;
	private NeoIdentityManagement im;
	
	// -----------------------------------------------------

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		store = new NeoDataStore();
		im = new NeoIdentityManagement(store);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		store.close();
	}
	
	// -----------------------------------------------------
	
	@Test
	public void testLogin() {
		final Context ctx = null;
		final SNClass identity = store.resolve(Aras.IDENTITY).asClass();
		final SNEntity user = identity.createInstance(ctx);
		
		Association.create(user, Aras.IDENTIFIED_BY, new SNText("Bud Spencer"), ctx);
		Association.create(user, Aras.HAS_CREDENTIAL, new SNText("bud"), ctx);
		Association.create(user, Aras.HAS_EMAIL, new SNText("BudSpencer@lichtflut.de"), ctx);
		store.attach(user);
		store.detach(user);
		
		try {
			im.login("Bud Spencer", new PasswordCredential("wrong password"));
		} catch (LoginException e) {
			Assert.assertEquals(ErrorCodes.LOGIN_USER_CREDENTIAL_NOT_MATCH, e.getErrCode());
		}
		
		try {
			im.login("Terence Hill", new PasswordCredential(null));
		} catch (LoginException e) {
			Assert.assertEquals(ErrorCodes.LOGIN_USER_NOT_FOUND, e.getErrCode());
		}
		
		try {
			final User loggedIn = im.login("Bud Spencer", new PasswordCredential("bud"));
			Assert.assertEquals("Bud Spencer", loggedIn.getName());
			Assert.assertEquals("BudSpencer@lichtflut.de", loggedIn.getEmail());
		} catch (LoginException e) {
			Assert.fail("User should have logged in.");
		}
		
	}
	
}
