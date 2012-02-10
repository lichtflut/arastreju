/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;

/**
 * <p>
 *  Exception for queries.
 * </p>
 *
 * <p>
 * 	Created Feb 10, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class QueryException extends ArastrejuRuntimeException {

	/**
	 * 
	 */
	public QueryException(String msg) {
		this(ErrorCodes.GENERAL_QUERY_ERROR, msg);
	}
	
	/**
	 * @param errCode
	 * @param msg
	 */
	public QueryException(Long errCode, String msg) {
		super(errCode, msg);
	}

}
