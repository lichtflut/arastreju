/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb.jdbc;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created 24.07.2012
 * </p>
 *
 * @author Raphael Esterle

 */
public enum Column {

	SUBJECT("sub"),
	PREDICATE("pre"),
	OBJECT("obj"),
	TYPE("type");
	
	private final String val;
	
	private Column(String value){
		val=value;
	}
	
	public String value(){
		return val;
	}
	
}
