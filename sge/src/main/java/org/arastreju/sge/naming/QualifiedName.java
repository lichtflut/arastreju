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
import java.util.UUID;

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
	
	public static final String LOCAL = "local:";
    public static final String UUID_NS = "local:uuid:";
    public static final String BLANK_NS = "local:blank:";

    // ----------------------------------------------------

	public static final String PREFIX_DELIM = ":";
	public static final String HASH = "#";
	public static final String SLASH = "/";
	
	private final String qn;
	
	private static final Map<String, QualifiedName> CACHE = new HashMap<String, QualifiedName>();
	
	//------------------------------------------------------
	
	public static boolean isUri(final String name){
		// TODO: Better URI check
		return name.contains(SLASH);
	}
	
	public static boolean isQname(final String name){
		return !isUri(name) && name.contains(PREFIX_DELIM);
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

    // ----------------------------------------------------

    /**
     * Generates a new qualified name in 'local:' namespace.
     * @return The new qualified name.
     */
    public static QualifiedName generate() {
        return new QualifiedName(UUID_NS + UUID.randomUUID().toString());
    }
	
	/**
	 * Create a new QualifiedName from a string - regarding a CACHE.
	 * @param qualifiedName The qualifed name.
	 * @return A new qualified name or the corresponding from CACHE.
	 */
	public static QualifiedName from(final String qualifiedName) {
		if (CACHE.containsKey(qualifiedName)) {
			return CACHE.get(qualifiedName);
		} else {
			final QualifiedName qn = new QualifiedName(qualifiedName);
			CACHE.put(qualifiedName, qn);
			return qn;
		}
	}
	
	/**
	 * Create a new QualifiedName for this URI - regarding a CACHE.
	 * @param namespace The namespace part
     * @param name The name part
	 * @return A new URI or the corresponding from CACHE.
	 */
	public static QualifiedName from(final String namespace, final String name) {
		return from(namespace + name);
	}

	//------------------------------------------------------

	/**
	 * Creates a qualified name for resource reference.
	 * @param uri The URI.
	 */
	private QualifiedName(final String uri){
		this.qn = uri;
	}
	
	// ----------------------------------------------------
	
	public String getSimpleName() {
		return getSimpleName(qn);
	}
	
	public String getNamespace(){
		return getNamespace(qn);
	}
	
	public String toURI() {
		return qn;
	}
	
	//------------------------------------------------------
	
	public int compareTo(QualifiedName other) {
		return qn.compareTo(other.qn);
	}
	
	@Override
	public String toString() {
		return qn;
	}
	
	@Override
	public int hashCode() {
		return qn.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QualifiedName) {
			QualifiedName other = (QualifiedName) obj;
			return qn.equals(other.qn);
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
