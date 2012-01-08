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
	
	public static final Long GENERAL_IO_ERROR = 8000L;
	public static final Long GRAPH_IO_ERROR = 8100L;
	public static final Long GRAPH_READ_ERROR = 8101L;
	public static final Long GRAPH_WRITE_ERROR = 8101L;

}
