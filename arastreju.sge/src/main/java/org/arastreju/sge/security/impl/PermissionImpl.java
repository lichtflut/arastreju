/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import static org.arastreju.sge.SNOPS.singleObject;
import static org.arastreju.sge.SNOPS.string;

import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.security.Permission;

import de.lichtflut.infra.Infra;

/**
 * <p>
 *  Implementation of a {@link Permission}.
 * </p>
 *
 * <p>
 * 	Created Sep 2, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class PermissionImpl implements Permission {
	
	private final ResourceNode node;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param node The node representing this permission.
	 */
	public PermissionImpl(final ResourceNode node) {
		this.node = node;
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Permission#getAssociatedResource()
	 */
	public ResourceNode getAssociatedResource() {
		return node;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.security.Permission#getName()
	 */
	public String getName() {
		return string(singleObject(node, Aras.HAS_UNIQUE_NAME));
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return node.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Permission) {
			final Permission other = (Permission) obj;
			return Infra.equals(node, other.getAssociatedResource());
		}
		return super.equals(obj);
	}

}
