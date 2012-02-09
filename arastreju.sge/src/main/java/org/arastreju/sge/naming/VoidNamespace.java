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
package org.arastreju.sge.naming;


/**
 * <p>
 * Pseudo namespace for names that are not in a namespace. E.g. local names.
 * </p>
 * 
 * <p>
 * Created: 06.07.2009
 * </p>
 * 
 * @author Oliver Tigges
 */
public class VoidNamespace implements Namespace {

	/**
	 * Constant for default UUID namespace URI.
	 */
	private static final String UUID = "http://arasteju.org/uuid#";

	private static final Namespace INSTANCE = new VoidNamespace();

	// ------------------------------------------------------

	/**
	 * Returns the reference to the Void Namespace.
	 */
	public static Namespace getInstance() {
		return INSTANCE;
	}

	// ------------------------------------------------------

	/**
	 * Private Constructor.
	 */
	private VoidNamespace() {
	}

	// ------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public String getUri() {
		return UUID;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPrefix() {
		return "aras-uuid";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRegistered() {
		return false;
	}

	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return UUID.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Namespace) {
			Namespace other = (Namespace) obj;
			return UUID.equals(other.getUri());
		}
		return super.equals(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getUri();
	}

}
