/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security;

/**
 * <p>
 *  Exception for login issues.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class LoginException extends SecurityException {

	/**
	 * @param errCode
	 * @param msg
	 * @param cause
	 */
	public LoginException(Long errCode, String msg, Throwable cause) {
		super(errCode, msg, cause);
	}

	/**
	 * @param errCode
	 * @param msg
	 */
	public LoginException(Long errCode, String msg) {
		super(errCode, msg);
	}

	/**
	 * @param errCode
	 */
	public LoginException(Long errCode) {
		super(errCode);
	}
	
}
