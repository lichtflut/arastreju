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

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import org.arastreju.bindings.rdb.jdbc.DBOperations;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.ArastrejuGateFactory;
import org.arastreju.sge.spi.GateInitializationException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class RdbGateFactory extends ArastrejuGateFactory {

	private final String DRIVER = "org.arastreju.bindings.rdb.jdbcDriver";
	private final String DB = "org.arastreju.bindings.rdb.db";
	private final String USER = "org.arastreju.bindings.rdb.dbUser";
	private final String PASS = "org.arastreju.bindings.rdb.dbPass";
	private final String PROTOCOL = "org.arastreju.bindings.rdb.protocol";
	//private final int MAX_CONNECTIONS = 10;
	
	// ----------------------------------------------------

	public RdbGateFactory(ArastrejuProfile profile) {
		super(profile);
	}

	// ----------------------------------------------------

	@Override
	public ArastrejuGate create(DomainIdentifier identifier)
			throws GateInitializationException {
		Connection con;
		try {
			con = getDataSource().getConnection();
			String storageName = identifier.getStorage();
			if (!DBOperations.tableExists(con, storageName))
				DBOperations.createTable(con, storageName);
			con.commit();
			con.close();
			return new RdbGate(getDataSource(), identifier);
		} catch (SQLException e) {
			throw new GateInitializationException();
		}
		
	}

	private ComboPooledDataSource getDataSource(){
		ComboPooledDataSource cpds = (ComboPooledDataSource) getProfile().getProfileObject(DB);
		if(null==cpds){
				ArastrejuProfile profile = getProfile();
				cpds = new ComboPooledDataSource();
				try {
					cpds.setDriverClass(profile.getProperty(DRIVER));
					cpds.setJdbcUrl(profile.getProperty(PROTOCOL)+ profile.getProperty(DB));
					cpds.setUser(profile.getProperty(USER));
					cpds.setPassword(profile.getProperty(PASS));
					
					cpds.setMinPoolSize(5);
					cpds.setAcquireIncrement(5);
					cpds.setMaxPoolSize(20);
				} catch (PropertyVetoException e) {
					throw new GateInitializationException();
				}
				
			getProfile().setProfileObject(DB, cpds);
			getDataSource();
		}
		return cpds;
	}
	
}
