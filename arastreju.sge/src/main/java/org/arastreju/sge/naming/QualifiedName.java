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

import java.io.Serializable;

import org.arastreju.sge.model.ResourceID;

/**
 * <p>
 * 	A qualified name consists of a global namespace and a name.
 * </p>
 * 
 * <p>
 * 	Created: 29.07.2008
 * </p>
 * 
 * @author Oliver Tigges
 */
public class QualifiedName implements Comparable<QualifiedName>, Serializable {
	
	public static final String VOID_NAMESPACE = "void";
	public static final String PREFIX_DELIM = ":";
	public static final String HASH = "#";
	public static final String SLASH = "/";
	
	private final String uri;
	
	//------------------------------------------------------
	
	public static boolean isUri(final String name){
		// TODO: Better URI check
		return name.indexOf(SLASH) > -1;
	}
	
	public static boolean isQname(final String name){
		return !isUri(name) && name.indexOf(PREFIX_DELIM) > -1;
	}
	
	public static String getSimpleName(final String name) {
		int pos = getSeperatorIndex(name);
		return name.substring(pos +1);
	}
	
	public static String getPrefix(final String qname) {
		int pos = getSeperatorIndex(qname);
		return qname.substring(0, pos);
	}
	
	/**
	 * Get the Namespace part of a full qualified name. The # is assumend to be part of namespace.
	 * @param name A qualified or unqualified name.
	 * @return The Namespace part.
	 */
	public static String getNamespace(final String name) {
		int pos = getSeperatorIndex(name);
		return name.substring(0, pos + 1);
	}
	
	//------------------------------------------------------
	
	/**
	 * Creates a new qualified name, where the prefix will be derived from managed namespace.
	 */
	public QualifiedName(final Namespace namespace, final String name) {
		this.uri = toURI(namespace, name);
	}
	
	/**
	 * Creates a new qualified name, where the prefix will be derived from managed namespace.
	 */
	public QualifiedName(final String namespace, final String name) {
		this.uri = toURI(namespace, name);
	}
	
	/**
	 * Creates a qualified name for resource reference.
	 * @param ref The {@link ResourceID}.
	 */
	public QualifiedName(final String uri){
		this(QualifiedName.getNamespace(uri), QualifiedName.getSimpleName(uri));
	}
	
	// ----------------------------------------------------
	
	/**
	 * Creates a new qualified name.
	 */
	protected QualifiedName(final Namespace namespace, final String prefix, final String name) {
		this.uri = toURI(namespace, name);
	}
	
	//------------------------------------------------------

	public String getSimpleName() {
		return getSimpleName(uri);
	}
	
	public Namespace getNamespace(){
		return new SimpleNamespace(uri);
	}
	
	public String toURI() {
		return uri;
	}
	
	//------------------------------------------------------
	
	public int compareTo(QualifiedName other) {
		return uri.compareTo(other.uri);
	}
	
	@Override
	public String toString() {
		return uri;
	}
	
	@Override
	public int hashCode() {
		return uri.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QualifiedName) {
			QualifiedName other = (QualifiedName) obj;
			return uri.equals(other.uri);
		}
		return false;
	}
	
	// -----------------------------------------------------
	
	protected static int getSeperatorIndex(final String name){
		int separatorIdx = name.indexOf(HASH);
		if (separatorIdx < 0) {
			separatorIdx = name.lastIndexOf(SLASH);
		}
		if (separatorIdx < 0) {
			separatorIdx = name.lastIndexOf(PREFIX_DELIM);
		}
		return separatorIdx;
	}
	
	private String toURI(String namespace, String name) {
		if (namespace != null){
			return namespace + name;
		} else {
			return VOID_NAMESPACE + HASH + name;
		}
	}
	
	private String toURI(Namespace namespace, String name) {
		if (namespace != null){
			return namespace.getUri() + name;
		} else {
			return VOID_NAMESPACE + HASH + name;
		}
	}
		
}
