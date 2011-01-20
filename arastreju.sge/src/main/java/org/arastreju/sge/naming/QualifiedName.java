/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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

import de.lichtflut.infra.Infra;

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
	
	private final Namespace namespace;
	private final String prefix;
	private final String name;
	
	//------------------------------------------------------
	
	public static boolean isUri(final String name){
		return name.indexOf(SLASH) > -1;
	}
	
	public static boolean isQname(final String name){
		return !isUri(name) && name.indexOf(PREFIX_DELIM) > -1;
	}
	
	public static String retrieveNamePart(final String name) {
		int pos = getSeperatorIndex(name);
		return name.substring(pos +1);
	}
	
	/**
	 * Get the Namespace part of a full qualified name. The # is assumend to be part of namespace.
	 * @param name A qualified or unqualified name.
	 * @return The Namespace part.
	 */
	public static String retrieveNamespaceUri(final String name) {
		int pos = getSeperatorIndex(name);
		return name.substring(0, pos + 1);
	}
	
	//------------------------------------------------------
	
	/**
	 * Creates a new qualified name.
	 */
	public QualifiedName(final Namespace namespace, final String prefix, final String name) {
		if (namespace == null || prefix == null || name == null){
			throw new IllegalStateException("got null value: namespace=" +
					 namespace + "; prefix="+ prefix +"; name=" + name);
		}
		this.namespace = namespace;
		this.prefix = prefix;
		this.name = name;
	}
	
	/**
	 * Creates a new qualified name, where the prefix will be derived from managed namespace.
	 */
	public QualifiedName(final Namespace namespace, final String name) {
		this.name = name;
		this.prefix = namespace.getDefaultPrefix();
		this.namespace = namespace;
	}
	
	/**
	 * Creates a new qualified name, where the prefix will be derived from managed namespace.
	 */
	public QualifiedName(final String namespace, final String name) {
		this.name = name;
		this.namespace = new SimpleNamespace(namespace);
		this.prefix = null;
	}
	
	/**
	 * Creates a qualified name for resource reference.
	 * @param ref The {@link ResourceID}.
	 */
	public QualifiedName(final ResourceID ref){
		this.namespace = ref.getNamespace();
		this.name = ref.getName();
		this.prefix = null;
	}
	
	/**
	 * Creates a qualified name for resource reference.
	 * @param ref The {@link ResourceID}.
	 */
	public QualifiedName(final String uri){
		this(QualifiedName.retrieveNamespaceUri(uri), QualifiedName.retrieveNamePart(uri));
	}
	
	//------------------------------------------------------

	public String getSimpleName() {
		return name;
	}
	
	public Namespace getNamespace(){
		return namespace;
	}
	
	public String toQName(){
		if (prefix != null){
			return prefix + PREFIX_DELIM + name;
		} else {
			return VOID_NAMESPACE + PREFIX_DELIM + name;
		}
	}
	
	public String toURI() {
		if (namespace != null){
			return namespace.getUri() + name;
		} else {
			return VOID_NAMESPACE + HASH + name;
		}
	}
	
	//------------------------------------------------------
	
	public int compareTo(QualifiedName other) {
		return this.toQName().compareTo(other.toQName());
	}
	
	@Override
	public String toString() {
		return toURI();
	}
	
	@Override
	public int hashCode() {
		return toURI().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		QualifiedName other = (QualifiedName) obj;
		boolean eq = Infra.equals(name, other.name) && Infra.equals(namespace, other.namespace);
		if (!eq && toURI().equals(other.toURI())){
			throw new IllegalStateException("Same URI but different namespace/name");
		}
		return eq;
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
