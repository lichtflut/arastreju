/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.junit.experimental.results.ResultMatchers;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

/**
 * <p>
 *  Database relatet operations witch work on an specific table.
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
	
	public static ArrayList<Map<String, String>> select(Connection con, String table, Map<String, String> conditions){
		Statement stm = createStatement(con);
		ArrayList<Map<String, String>> result = new ArrayList<Map<String,String>>();
		try {
			ResultSet rs = stm.executeQuery(SQLQueryBuilder.createSelect(table, conditions));
			ResultSetMetaData meta;
			while(rs.next()){
				meta = rs.getMetaData();
				HashMap<String, String> temp = new HashMap<String, String>();
				for (int i = 0; i < meta.getColumnCount(); i++) {
					temp.put(meta.getColumnLabel(i+1), rs.getString(i+1));
				}
				result.add(temp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private static Statement createStatement(Connection con){
		try {
			return con.createStatement();
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR);
		}
	}
	
}
