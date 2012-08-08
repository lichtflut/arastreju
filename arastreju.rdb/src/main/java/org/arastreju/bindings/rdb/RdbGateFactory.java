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

import org.arastreju.bindings.rdb.jdbc.DBOperations;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.ArastrejuGateFactory;
import org.arastreju.sge.spi.GateInitializationException;

public class RdbGateFactory extends ArastrejuGateFactory {
	
	private final String DRIVER ="org.arastreju.bindings.rdb.jdbcDriver";
	private final String DB = "org.arastreju.bindings.rdb.db";
	private final String USER = "org.arastreju.bindings.rdb.dbUser";
	private final String PASS = "org.arastreju.bindings.rdb.dbPass";
	private final String PROTOCOL = "org.arastreju.bindings.rdb.protocol";
	private final int MAX_CONNECTIONS = 10;
	
	// ----------------------------------------------------
	
	public RdbGateFactory(ArastrejuProfile profile) {
		super(profile);
	}
	
	// ----------------------------------------------------
	
	@Override
	public ArastrejuGate create(DomainIdentifier identifier) throws GateInitializationException {
		
		String storageName = identifier.getStorage().toUpperCase();
		
		ArastrejuProfile profile = getProfile();
		
		RdbConnectionProvider provider = new RdbConnectionProvider(
				profile.getProperty(DRIVER),
				profile.getProperty(USER),
				profile.getProperty(PASS),
				profile.getProperty(PROTOCOL)+profile.getProperty(DB),
				storageName,
				MAX_CONNECTIONS);
		
		Connection con = provider.getConnection();
		if(!DBOperations.tableExists(con, storageName))
			DBOperations.createTable(con, storageName);
		provider.close(con);
		
		return new RdbGate(provider, identifier);
	}

}
