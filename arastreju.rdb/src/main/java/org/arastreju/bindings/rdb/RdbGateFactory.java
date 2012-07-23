/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb;

/**
 * <p>
 *  RRdb specific extension of ArastrejuGateFactory. 
 * </p>
 *
 * <p>
 * 	Created 23.07.2012
 * </p>
 *
 * @author Raphael Esterle
 */

import java.sql.Connection;

import org.arastreju.rdb.db.DB;
import org.arastreju.rdb.db.DBInit;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.ArastrejuGateFactory;
import org.arastreju.sge.spi.GateInitializationException;

public class RdbGateFactory extends ArastrejuGateFactory {

	public RdbGateFactory(ArastrejuProfile profile) {
		super(profile);
	}

	@Override
	public ArastrejuGate create(DomainIdentifier identifier) throws GateInitializationException {
		
		String storageName = identifier.getStorage();
		
		// TODO: check is storage with given name already exists.
		
		ArastrejuProfile profile = getProfile();
		
		RdbConnectionProvider provider = new RdbConnectionProvider(
				profile.getProperty(DB.PROFILE_DRIVER.val()),
				profile.getProperty(DB.PROFILE_USER.val()),
				profile.getProperty(DB.PROFILE_PASS.val()),
				profile.getProperty(DB.PROFILE_PROTOCOL.val())+profile.getProperty(DB.PROFILE_DB.val()),
				10);
		
		Connection con = provider.getConnection();
		if(!DBInit.tableExists(con, storageName))
			DBInit.init(con, storageName);
		provider.close(con);
		
		return new RdbGate(provider, identifier);
	}

}
