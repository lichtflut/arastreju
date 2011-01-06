/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.spi;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jan 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class GateInitializationException extends RuntimeException {

	/**
	 * 
	 */
	public GateInitializationException() {
	}

	/**
	 * @param msg
	 */
	public GateInitializationException(final String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 */
	public GateInitializationException(final Throwable msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public GateInitializationException(final String msg, Throwable cause) {
		super(msg, cause);
	}

}
