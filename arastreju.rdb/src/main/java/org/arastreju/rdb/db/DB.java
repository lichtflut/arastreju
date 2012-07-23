/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.rdb.db;

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

public enum DB {
	
	PROFILE_DRIVER("org.arastreju.bindings.rdb.jdbcDriver"),
	PROFILE_DB("org.arastreju.bindings.rdb.db"),
	PROFILE_USER("org.arastreju.bindings.rdb.dbUser"),
	PROFILE_PASS("org.arastreju.bindings.rdb.dbPass"),
	PROFILE_PROTOCOL("org.arastreju.bindings.rdb.protocol"),
	TABLE("aras"),
	COL_SUBJECT("sub"),
	COL_PREDICATE("pre"),
	COL_OBJECT("obj"),
	COL_TYPE("type");
	
	private final String val;
	
	private DB(String value){
		val=value;
	}
	
	public String val(){
		return val;
	}
}
