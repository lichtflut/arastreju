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
package org.arastreju.sge.naming;

/**
 * <p>
 * 	A globally unique namespace.
 * </p>
 * 
 * <p>
 * 	Created: 06.07.2009
 * </p>
 *
 * @author Oliver Tigges 
 */
public interface Namespace extends Comparable<Namespace> {

	/**
	 * The URI of this namespace.
	 * @return The URI of this namespace.
	 */
	String getUri();
	
	/**
	 * Get the default prefix. This prefix is neither mandatory nor unique! 
	 * @return The default prefix.
	 */
	String getDefaultPrefix();
	
	/**
	 * Checks if this namespace is a namespace is registered and thus
	 * is (or can be) persisted.
	 * @return True if namespace is registered.
	 */
	boolean isRegistered();

}