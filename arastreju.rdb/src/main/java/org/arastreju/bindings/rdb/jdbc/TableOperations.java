/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb.jdbc;

import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.naming.QualifiedName;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Database related operations witch work on an specific table.
 * </p>
 *
 * <p>
 * 	Created 23.07.2012
 * </p>
 *
 * @author Raphael Esterle
 */

public class TableOperations {
	
	public static void insert(Connection con, String table, String subject, String predicate, String object, String type){
		Map<String, String> conditions = createConditionMap();
		conditions.put(Column.SUBJECT.value(), subject);
		conditions.put(Column.PREDICATE.value(), predicate);
		conditions.put(Column.OBJECT.value(), object);
		conditions.put(Column.TYPE.value(), type);
		exQuery(con, SQLQueryBuilder.createInsert(table, conditions));
	}
	
	public static boolean deleteAssosiation(Connection con, String table, String subject, String predicate, String object){
		Map< String, String> conditions = new HashMap<String, String>();
		conditions.put(Column.SUBJECT.value(), subject);
		conditions.put(Column.PREDICATE.value(), predicate);
		conditions.put(Column.OBJECT.value(), object);
		if(exUpdate(con, SQLQueryBuilder.createDelete(table, conditions))>0)
			return true;
		return false;
	}
	
	public static void deleteOutgoingAssosiations(Connection con, String table, String uri){
		Map< String, String> conditions = new HashMap<String, String>();
		conditions.put(Column.SUBJECT.value(), uri);
		exQuery(con, SQLQueryBuilder.createDelete(table, conditions));
	}
	
	public static void deleteIncommingAssosiations(Connection con, String table, String uri){
		Map< String, String> conditions = new HashMap<String, String>();
		conditions.put(Column.OBJECT.value(), uri);
		exQuery(con, SQLQueryBuilder.createDelete(table, conditions));
	}
	
	public static List<Map<String, String>> getOutgoingAssosiations(Connection con, String table, QualifiedName qn){
		HashMap<String, String> conditions = new HashMap<String, String>();
		conditions.put(Column.SUBJECT.value(), qn.toURI());
		ArrayList<Map<String, String>> res = select(con, table, conditions);
		return res;
	}
	
	public static List<Map<String, String>> getIncommingAssosiations(Connection con, String table, QualifiedName qn){
		HashMap<String, String> conditions = new HashMap<String, String>();
		conditions.put(Column.OBJECT.value(), qn.toURI());
		ArrayList<Map<String, String>> res = select(con, table, conditions);
		return res;
	}
	
	private static ArrayList<Map<String, String>> select(Connection con, String table, Map<String, String> conditions){
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
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR, "SQL ERROR: "+e.getMessage());
		}
		
		return result;
	}
	
	public static boolean hasOutgoingAssosiations(Connection con, String table, QualifiedName qn){
		String query = "SELECT 1 FROM "+table+" WHERE "+Column.SUBJECT.value()+"='"+qn.toURI()+"' LIMIT 1;";
		ResultSet rs = exQuery(con, query);
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR, e.getMessage());
		}
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
	
	private static int exUpdate(Connection con, String query){
		Statement stm = createStatement(con);
		try {
			return stm.executeUpdate(query);
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
	
	private static Map<String, String> createConditionMap(){
		return new HashMap<String, String>();
	}
}
