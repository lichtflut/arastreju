/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.security.impl;

import static org.arastreju.sge.SNOPS.singleObject;
import static org.arastreju.sge.SNOPS.string;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
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
public class DomainImpl implements Domain {
	
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
		final SemanticNode idNode = SNOPS.singleObject(node, Aras.HAS_UNIQUE_NAME);
		if (idNode == null) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE, "Domain has no name!");
		}
		return idNode.asValue().getStringValue();
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getTitle() {
		return string(singleObject(node, Aras.HAS_TITLE));
	}

	/** 
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return string(singleObject(node, Aras.HAS_DESCRIPTION));
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
