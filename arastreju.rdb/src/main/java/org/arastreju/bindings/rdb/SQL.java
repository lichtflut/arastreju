/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created 21.08.2012
 * </p>
 *
 * @author Raphael Esterle

 */
public enum SQL {
	
	AND("AND"),
	OR("OR"),
	SELECT("SELECT"),
	DELETE("DELETE"),
	WHITESP(" "),
	BRACKET_OPEN("("),
	BRACKET_CLOSE(")"),
	NOT("NOT"),
	QUOTE("'"),
	EQUALS("=");
	
	private final String val;
	
	private SQL(String value){
		val=value;
	}
	
	public String value(){
		return val;
	}
}
