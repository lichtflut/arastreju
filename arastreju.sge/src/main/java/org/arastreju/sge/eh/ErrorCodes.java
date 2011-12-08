/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.eh;

/**
 * <p>
 *  Error Codes for Arastreju.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ErrorCodes {
	
	public static final Long UNKNOWN_ERROR = 0L;
	
	public static final Long INITIALIZATION_EXCEPTION = 100L;
	
	public static final Long GENERAL_SECURITY_ERROR = 1000L;
	public static final Long LOGIN_FAILED = 1100L;
	public static final Long LOGIN_USER_NOT_FOUND = 1101L;
	public static final Long LOGIN_USER_CREDENTIAL_NOT_MATCH = 1102L;
	public static final Long LOGIN_INVALID_DATA = 1103L;
	
	public static final Long REGISTRATION_FAILED = 1200L;
	public static final Long REGISTRATION_NAME_ALREADY_IN_USE = 1201L;
	
	public static final Long GENERAL_RUNTIME_ERROR = 2000L;
	public static final Long GENERAL_CONSISTENCY_FAILURE = 2000L;

}
