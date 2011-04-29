/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security;

import eh.ArastrejuException;

/**
 * <p>
 *  Exception for security issues.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class SecurityException extends ArastrejuException {

	/**
	 * @param errCode
	 * @param msg
	 * @param cause
	 */
	public SecurityException(Long errCode, String msg, Throwable cause) {
		super(errCode, msg, cause);
	}

	/**
	 * @param errCode
	 * @param msg
	 */
	public SecurityException(Long errCode, String msg) {
		super(errCode, msg);
	}

	/**
	 * @param errCode
	 */
	public SecurityException(Long errCode) {
		super(errCode);
	}
	

}
