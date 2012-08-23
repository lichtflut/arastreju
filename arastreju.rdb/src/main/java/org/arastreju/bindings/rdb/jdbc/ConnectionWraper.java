/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created 17.08.2012
 * </p>
 *
 * @author Raphael Esterle

 */
public class ConnectionWraper {
	
	private Connection  con = null;
	private final String table;
	private static ComboPooledDataSource pool;
	
	public ConnectionWraper(ComboPooledDataSource cpds, String table){
		pool = cpds;
		this.table=table;
	}
	
	public Connection getConnection() throws SQLException{
		if(null==con||con.isClosed())
			con=pool.getConnection();
		return con;
	}
	
	public String getTable(){
		return table;
	}
	
}
