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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
	
	private static final Map<String, QualifiedName> cache = new HashMap<String, QualifiedName>();
	
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
	
	/**
	 * Create a new QualifiedName for this URI - regarding a cache.
	 * @param uri The URI.
	 * @return A new URI or the corresponding from cache.
	 */
	public static QualifiedName create(final String uri) {
		if (cache.containsKey(uri)) {
			return cache.get(uri);
		} else {
			final QualifiedName qn = new QualifiedName(uri);
			cache.put(uri, qn);
			return qn;
		}
	}
	
	/**
	 * Create a new QualifiedName for this URI - regarding a cache.
	 * @param namespace The namespace part
     * @param name The name part
	 * @return A new URI or the corresponding from cache.
	 */
	public static QualifiedName create(final String namespace, final String name) {
		return create(namespace + name);
	}
	
	//------------------------------------------------------
	
	/**
	 * Creates a new qualified name, where the prefix will be derived from managed namespace.
	 */
	public QualifiedName(final String namespace, final String name) {
		this(namespace + name);
	}
	
	/**
	 * Creates a qualified name for resource reference.
	 * @param uri The URI.
	 */
	public QualifiedName(final String uri){
		this.uri = uri;
	}
	
	// ----------------------------------------------------
	
	public String getSimpleName() {
		return getSimpleName(uri);
	}
	
	public String getNamespace(){
		return getNamespace(uri);
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
	
}
