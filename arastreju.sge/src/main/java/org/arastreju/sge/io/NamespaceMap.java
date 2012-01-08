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
package org.arastreju.sge.io;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.arastreju.sge.naming.Namespace;

/**
 * <p>
 *  Map for namespaces with a unique prefix.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NamespaceMap {
	
	private final Map<String, Namespace> prefixMap = new HashMap<String, Namespace>();
	
	private final Set<Namespace> namespaces = new HashSet<Namespace>();
	
	private int nextId = 1; 
	
	// -----------------------------------------------------
	
	/**
	 * Default constructor.
	 */
	public NamespaceMap() {
	}
	
	/**
	 * Constructor with initial collection of Namespaces.
	 * @param namespaces Collection of Namespaces.
	 */
	public NamespaceMap(final Collection<Namespace> namespaces) {
		addNamepaces(namespaces);
	}
	
	// -----------------------------------------------------

	/**
	 * @return the prefixMap
	 */
	public Map<String, Namespace> getPrefixMap() {
		return prefixMap;
	}
	
	public Set<String> getPrefixes(){
		return new TreeSet<String>(prefixMap.keySet());
	}
	
	/**
	 * @return the namespaces
	 */
	public Set<Namespace> getNamespaces() {
		return namespaces;
	}
	
	/** Check if there exists a namespace for given prefix.
	 * @param prefix The prefix.
	 * @return true if there is a namespace.
	 */
	public boolean containsPrefix(final String prefix){
		return prefixMap.containsKey(prefix);
	}
	 
	/**
	 * Get the namespace for given prefix.
	 * @param prefix The prefix.
	 * @return The corresponding Namespace.
	 */
	public Namespace getNamespace(final String prefix){
		return prefixMap.get(prefix);
	}
	
	// -----------------------------------------------------
	
	public void addNamepaces(final Collection<Namespace> namespaces){
		for (Namespace namespace : namespaces) {
			addNamespace(namespace);
		}
	}
	
	public synchronized void addNamespace(final Namespace namespace){
		if (!this.namespaces.contains(namespace)){
			this.namespaces.add(namespace);
			prefixMap.put(prefix(namespace), namespace);
		}
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (String prefix : getPrefixes()) {
			sb.append(prefix + " --> " + getNamespace(prefix) + "\n");
		}
		return sb.toString();
	}
	
	// -----------------------------------------------------
	
	private String prefix(final Namespace namespace){
		final String defaultPrefix = namespace.getPrefix();
		if (defaultPrefix != null && !prefixMap.containsKey(defaultPrefix)){
			return namespace.getPrefix();
		} else {
			return "ns" + nextId++;
		}
	}

}
