/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.spi;

/**
 * <p>
 *  Exception to be thrown when a gate cannot be initialized.
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
	public GateInitializationException(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 */
	public GateInitializationException(Throwable msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public GateInitializationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
