/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
