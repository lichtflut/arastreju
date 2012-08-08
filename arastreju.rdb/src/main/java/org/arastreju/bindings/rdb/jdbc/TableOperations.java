/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb.jdbc;

import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		ArrayList<Map<String, String>> result = new ArrayList<Map<String,String>>();
		ResultSet rs = exQuery(con, SQLQueryBuilder.createSelect(table, conditions));
		ResultSetMetaData meta;
		try {
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
	
	public static void deleteAssosiation(Connection con, String table, String subject, String predicate, String object){
		exQuery(con, SQLQueryBuilder.createDelete(table, subject, predicate, object));
	}
	
	public static void deleteOutgoingAssosiations(Connection con, String table, String subject){
		exQuery(con, SQLQueryBuilder.deleteOutgoingAssosiations(table, subject));
	}
	
	public static void deleteIncommingAssosiations(Connection con, String table, String object){
		exQuery(con, SQLQueryBuilder.deleteOutgoingAssosiations(table, object));
	}
	
	private static ResultSet exQuery(Connection con, String query){
		Statement stm = createStatement(con);;
		ResultSet rs;
		try {
			rs = stm.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.GRAPH_IO_ERROR, "SQL ERROR: "+e.getMessage());
		}
	}
	
	private static Statement createStatement(Connection con){
		try {
			return con.createStatement();
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR);
		}
	}
	
}
