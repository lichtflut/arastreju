/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.context;

import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.nodes.views.SNContext;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Simple identifier of a Context.
 * </p>
 *
 * <p>
 * 	Created Jun 16, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class SimpleContextID extends SimpleResourceID implements Context {

	/**
	 * @param qn
	 */
	public SimpleContextID(QualifiedName qn) {
		super(qn);
	}

	/**
	 * @param nsUri
	 * @param name
	 */
	public SimpleContextID(String nsUri, String name) {
		super(nsUri, name);
	}

	/**
	 * @param namespace
	 * @param name
	 */
	public SimpleContextID(Namespace namespace, String name) {
		super(namespace, name);
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.SimpleResourceID#asResource()
	 */
	@Override
	public SNContext asResource() {
		return new SNContext(super.asResource());
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final Context other) {
		return getQualifiedName().compareTo(other.getQualifiedName());
	}

}
