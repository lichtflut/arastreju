/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;

/**
 * <p>
 *  
 * </p>
 *
 * <p>
 * 	Created 23.07.2012
 * </p>
 *
 * @author Raphael Esterle
 */

public class TableOperations {
	
	public static void insert(Connection con, String table, Map<String, String> columns){
		Statement stm = createStatement(con);
		try {
			stm.execute(SQLQueryBuilder.createInsert(table, columns));
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR, "SQL ERROR: "+e.getMessage());
		}
	}
	
	public static Map<String, String> select(){
		return null;
	}
	
	private static Statement createStatement(Connection con){
		try {
			return con.createStatement();
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR);
		}
	}
	
}
