/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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


import org.arastreju.sge.model.Infra;

import java.io.Serializable;

/**
 * <p>
 * 	A simple namespace implementation.
 * </p>
 * 
 * <p>
 * 	Created: July 14, 2009
 * </p>
 *
 * @author Oliver Tigges
 */
public class SimpleNamespace implements Namespace, Comparable<Namespace>, Serializable {
	
	private String uri;
	
	private String prefix;
	
	// -----------------------------------------------------

	/**
	 * Constructor.
	 * @param uri The URI of this namespace.
	 */
	public SimpleNamespace(final String uri) {
		this.uri = uri;
	}
	
	/**
	 * Creates a unregistered namespace instance with prefix.
	 * @param uri The URI.
	 * @param prefix The default prefix.
	 */
	public SimpleNamespace(final String uri, final String prefix) {
		this.uri = uri;
		this.prefix = prefix;
	}
	
	// -----------------------------------------------------

    @Override
	public String getUri() {
		return uri;
	}

    @Override
	public String getPrefix() {
		return prefix;
	}

	// -----------------------------------------------------

	@Override
	public int hashCode() {
		return uri.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Namespace){
			Namespace other = (Namespace) obj;
			return uri.equals(other.getUri());
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return uri;
	}

    @Override
	public int compareTo(final Namespace other) {
		return Infra.compare(getUri(), other.getUri());
	}

}
