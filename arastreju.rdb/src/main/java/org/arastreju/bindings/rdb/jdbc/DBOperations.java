/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;

/**
 * <p>
 *  RRdb specific extension of AbstractConversationContext. 
 * </p>
 *
 * <p>
 * 	Created 23.07.2012
 * </p>
 *
 * @author Raphael Esterle
 */

public class DBOperations {
	
	public static void createTable(Connection con, String tableName){
		
		String fieldType = "varchar(255)";
		String ws = " ";
		String de = ", ";
		StringBuilder sb = new StringBuilder("CREATE TABLE ");
		sb.append(tableName);
		sb.append('(');
		sb.append(ws);
		
		for(Column col : Column.values()){
			sb.append(col.value());
			sb.append(ws);
			sb.append(fieldType);
			sb.append(de);
		}
		
		sb.setCharAt(sb.length()-1, ';');
		sb.setCharAt(sb.length()-2, ')');
		
		System.out.println(sb.toString());
		
		try {
			Statement smt = con.createStatement();
			smt.execute(sb.toString());
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR, "SQL Error:"+e.getMessage());
		}
	}
	
	public static void deleteTable(Connection con, String tablename){
		try {
			Statement smt = con.createStatement();
			smt.execute("DROP TABLE "+tablename+";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean tableExists(Connection con, String name){
		try {
			DatabaseMetaData meta = con.getMetaData();
			return meta.getTables(null, null, name, null).next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
