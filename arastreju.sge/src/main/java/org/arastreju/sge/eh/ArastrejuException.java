/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.eh;

/**
 * <p>
 *  Checked Arastreju exception.
 *  @see ErrorCodes
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ArastrejuException extends Exception {

	private final Long errCode;

	// -----------------------------------------------------
	
	/**
	 * Constructor;
	 */
	public ArastrejuException(final Long errCode) {
		this.errCode = errCode;
	}
	
	/**
	 * Constructor;
	 */
	public ArastrejuException(final Long errCode, final String msg) {
		super(msg);
		this.errCode = errCode;
	}
	
	/**
	 * Constructor;
	 */
	public ArastrejuException(final Long errCode, final String msg, final Throwable cause) {
		super(msg, cause);
		this.errCode = errCode;
	}
	
	// -----------------------------------------------------
	
	/**
	 * @return the errCode
	 */
	public Long getErrCode() {
		return errCode;
	}

}
