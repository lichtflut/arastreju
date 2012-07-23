/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.rdb.db;

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

public class DBInit {
	
	public static void init(Connection con, String storageName){
		System.out.println("CON: "+con);
		try {
			Statement smt = con.createStatement();
			smt.execute("CREATE TABLE "+storageName+" (" +
					"subject varchar(255), "+
					"predicate varchar(255), "+
					"object varchar(255));");
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR, "SQL Error:"+e.getMessage());
		}
	}
	
	public static boolean tableExists(Connection con, String name){
		try {
			DatabaseMetaData meta = con.getMetaData();
			return meta.getTables(null, null, name, null).next();
		} catch (SQLException e) {
			System.out.println("hlalalalala");
			e.printStackTrace();
		}
		return false;
	}
}
