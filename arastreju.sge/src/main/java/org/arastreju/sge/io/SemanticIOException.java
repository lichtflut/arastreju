/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.io;

import org.arastreju.sge.eh.ArastrejuException;
import org.arastreju.sge.eh.ErrorCodes;

/**
 * <p>
 *  Exception for input and output of semantic data.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class SemanticIOException extends ArastrejuException {

	/**
	 * @param errCode
	 */
	public SemanticIOException(String msg) {
		super(ErrorCodes.GRAPH_IO_ERROR, msg);
	}
	
	/**
	 * @param errCode
	 * @param msg
	 */
	public SemanticIOException(Long errCode, String msg) {
		super(errCode, msg);
	}

	/**
	 * @param errCode
	 */
	public SemanticIOException(Long errCode) {
		super(errCode);
	}

	/**
	 * @param errCode
	 * @param msg
	 * @param cause
	 */
	public SemanticIOException(Long errCode, String msg, Throwable cause) {
		super(errCode, msg, cause);
	}

	

}
