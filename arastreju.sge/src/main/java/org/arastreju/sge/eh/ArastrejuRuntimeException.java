/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.eh;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ArastrejuRuntimeException extends RuntimeException {

	private final Long errCode;

	// -----------------------------------------------------
	
	/**
	 * Constructor;
	 */
	public ArastrejuRuntimeException(final Long errCode) {
		this.errCode = errCode;
	}
	
	/**
	 * Constructor;
	 */
	public ArastrejuRuntimeException(final Long errCode, final String msg) {
		super(msg);
		this.errCode = errCode;
	}
	
	/**
	 * Constructor;
	 */
	public ArastrejuRuntimeException(final Long errCode, final String msg, final Throwable cause) {
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
