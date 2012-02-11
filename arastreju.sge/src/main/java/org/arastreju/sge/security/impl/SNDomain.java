/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import static org.arastreju.sge.SNOPS.singleObject;

import java.io.Serializable;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.ResourceView;
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
public class SNDomain extends ResourceView implements Domain, Serializable {
	
	/**
	 * Default constructor. 
	 */
	public SNDomain() {
	}
	
	/**
	 * Constructor.
	 * @param node The associated node representing the domain.
	 */
	public SNDomain(ResourceNode node) {
		super(node);
	}
	
	// ----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public String getUniqueName() {
		return stringValue(Aras.HAS_UNIQUE_NAME);
	}
	
	public void setUniqueName(String name) {
		setValue(Aras.HAS_UNIQUE_NAME, name);
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getTitle() {
		return stringValue(Aras.HAS_TITLE);
	}
	
	/**
	 * Set the title.
	 * @param title The title
	 */
	public void setTitle(final String title) {
		SNOPS.assure(this, Aras.HAS_TITLE, new SNText(title), Aras.IDENT);
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return stringValue(Aras.HAS_DESCRIPTION);
	}
	
	/**
	 * Set the description.
	 * @param desc The description
	 */
	public void setDescription(final String desc) {
		SNOPS.assure(this, Aras.HAS_DESCRIPTION, new SNText(desc), Aras.IDENT);
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean isDomesticDomain() {
		final SemanticNode mdNode = singleObject(this, Aras.IS_DOMESTIC_DOMAIN);
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
