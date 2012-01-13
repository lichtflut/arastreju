/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import static org.arastreju.sge.SNOPS.singleObject;
import static org.arastreju.sge.SNOPS.string;

import java.io.Serializable;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.security.Domain;

/**
 * <p>
 *  The implementation of {@link Domain}.
 * </p>
 *
 * <p>
 * 	Created Jan 11, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class DomainImpl implements Domain, Serializable {
	
	private final ResourceNode node;
	
	// ----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param node The associated node representing the domain.
	 */
	public DomainImpl(ResourceNode node) {
		this.node = node;
	}
	
	// ----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public ResourceNode getAssociatedResource() {
		return node;
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getUniqueName() {
		return string(singleObject(node, Aras.HAS_UNIQUE_NAME));
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getTitle() {
		return string(singleObject(node, Aras.HAS_TITLE));
	}
	
	/**
	 * Set the title.
	 * @param title The title
	 */
	public void setTitle(final String title) {
		SNOPS.assure(node, Aras.HAS_TITLE, new SNText(title), Aras.IDENT);
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return string(singleObject(node, Aras.HAS_DESCRIPTION));
	}
	
	/**
	 * Set the description.
	 * @param desc The description
	 */
	public void setDescription(final String desc) {
		SNOPS.assure(node, Aras.HAS_DESCRIPTION, new SNText(desc), Aras.IDENT);
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean isMasterDomain() {
		final SemanticNode mdNode = singleObject(node, Aras.IS_MASTER_DOMAIN);
		if (mdNode != null && mdNode.isValueNode()) {
			return mdNode.asValue().getBooleanValue();
		} else {
			return false;
		}
	}
	
	// ----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getUniqueName();
	}

}
