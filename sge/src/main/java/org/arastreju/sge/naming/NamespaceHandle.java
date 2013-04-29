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


/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Dec 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NamespaceHandle implements Namespace {
	
	private String uri;
	
	private String prefix;
	
	// -----------------------------------------------------

	/**
	 * Constructor.
	 * @param uri The URI.
	 * @param prefix The prefix.
	 */
	public NamespaceHandle(String uri, String prefix) {
		this.uri = uri;
		this.prefix = prefix;
	}
	
	// ----------------------------------------------------
	
	/** 
	* {@inheritDoc}
	*/
	public String getUri() {
		return uri;
	}

	/** 
	* {@inheritDoc}
	*/
	public String getPrefix() {
		return prefix;
	}

	// ----------------------------------------------------
	
	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (uri != null) {
			return uri.hashCode();
		} else {
			return super.hashCode();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (uri == null) {
			return false;
		}
		if (obj instanceof Namespace){
			Namespace other = (Namespace) obj;
			return uri.equals(other.getUri());
		}
		return super.equals(obj);
	}
	
	/** 
	* {@inheritDoc}
	*/
	@Override
	public String toString() {
		return "NsHandle(prefix="+ prefix + ";uri=" + uri +")";
	}
	
}
