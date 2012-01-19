/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.ogm.impl;

import org.arastreju.ogm.EntityNodeManager;
import org.arastreju.ogm.impl.enhance.EnhancedEntityNode;
import org.arastreju.ogm.impl.enhance.EntityEnhancer;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jan 18, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class EntityNodeManagerImpl implements EntityNodeManager {
	
	private final ArastrejuGate gate;
	
	// ----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param gate The gate to arastreju.
	 */
	public EntityNodeManagerImpl(ArastrejuGate gate) {
		this.gate = gate;
	}
	
	// ----------------------------------------------------

	/** 
	 * {@inheritDoc}
	 */
	public Object find(ResourceID id) {
		final ResourceNode node = gate.startConversation().findResource(id.getQualifiedName());
		return node;
	}

	/** 
	 * {@inheritDoc}
	 */
	public <T> T find(Class<T> type, ResourceID id) {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	public void attach(Object entity) {
		if (entity instanceof EnhancedEntityNode) {
			final EnhancedEntityNode enhanced = (EnhancedEntityNode) entity;
			final ResourceNode node = enhanced.aras$getResourceNode();
			gate.startConversation().attach(node);
		} else {
			final EnhancedEntityNode proxy = new EntityEnhancer().proxy(entity);
			final ResourceID id = proxy.aras$getID();
			if (id != null) {
				final ResourceNode existing = gate.startConversation().findResource(id.getQualifiedName());
				if (existing != null) {
					// TODO: merge
				} else {
					proxy.aras$setResourceNode(new SNResource(id.getQualifiedName()));
				}
			} else {
				proxy.aras$setResourceNode(new SNResource());
			}
		}
	}

}
