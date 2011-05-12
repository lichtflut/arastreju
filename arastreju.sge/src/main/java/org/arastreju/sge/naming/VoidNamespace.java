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

import java.io.Serializable;

import org.arastreju.sge.model.ResourceID;

/**
 * <p>
 * 	Pseudo namespace for names that are not in a namespace. E.g. local names.
 * </p>
 * 
 * <p>
 * 	Created: 06.07.2009
 * </p>
 *
 * @author Oliver Tigges 
 */
public class VoidNamespace implements Namespace, Serializable {
	
	/**
	 * Constant for void namespace URI.
	 */
	private static final String VOID = "http://void.arasteju.org#";
	
	private static final Namespace INSTANCE = new VoidNamespace();

	// ------------------------------------------------------
	
	/**
	 * Returns the reference to the Void Namespace.
	 */
	public static Namespace getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Checks if given namespace references void namespace.
	 * @param namespace The namespace to be checked.
	 * @return true if given namespace represents the void namespace.
	 */
	public static boolean isVoidNamespace(final Namespace namespace) {
		return INSTANCE.equals(namespace);
	}
	
	/**
	 * Checks if given URI references void namespace.
	 * @param uri The URI to be checked.
	 * @return true if URI references the void namespace.
	 */
	public static boolean isVoidNamespace(final String uri) {
		return VOID.equals(uri);
	}
	
	/**
	 * Checks if given resource reference is in the void namespace.
	 * @param uri The URI of a resource to be checked.
	 * @return true if URI references the void namespace.
	 */
	public static boolean isInVoidNamespace(final ResourceID ref) {
		return INSTANCE.equals(ref.getNamespace());
	}
	
	// ------------------------------------------------------
	
	/**
	 * Private Constructor.
	 */
	private VoidNamespace() {
	}
	
	// ------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.model.semantic.Namespace#getUri()
	 */
	public String getUri() {
		return VOID;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.Namespace#getDefaultPrefix()
	 */
	public String getDefaultPrefix() {
		return "void";
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.model.semantic.Namespace#isManaged()
	 */
	public boolean isRegistered() {
		return false;
	}
	
	// -----------------------------------------------------
	
	public void setDefaultPrefix(final String prefix) {
		throw new RuntimeException("Not supported");
	}

	public void setUri(final String uri) {
		throw new RuntimeException("Not supported");
	}
	
	// ------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return VOID.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Namespace){
			Namespace other = (Namespace) obj;
			return VOID.equals(other.getUri());
		}
		return super.equals(obj);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getUri();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final Namespace other) {
		return -1;
	}

}
