/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.bindings.neo4j.extensions;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.impl.AssociationHandler;
import org.arastreju.sge.model.associations.AbstractAssociationKeeper;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Special {@link AssociationKeeper} for Neo4J.
 * </p>
 *
 * <p>
 * 	Created Oct 11, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoAssociationKeeper extends AbstractAssociationKeeper implements NeoConstants, Serializable {
	
	private final ResourceNode arasNode;
	
	private final Node neoNode;
	
	private final AssociationHandler handler;
	
	private final Logger logger = LoggerFactory.getLogger(NeoAssociationKeeper.class);
	
	// -----------------------------------------------------
	
	/**
	 * Create a new association keeper.
	 * @param arasNode The aras node
	 * @param neoNode The neo node.
	 */
	public NeoAssociationKeeper(final ResourceNode arasNode, final Node neoNode, final AssociationHandler handler) {
		this.arasNode = arasNode;
		this.neoNode = neoNode;
		this.handler = handler;
	}
	
	// -----------------------------------------------------
	
	public Node getNeoNode() {
		return neoNode;
	}

	public ResourceNode getArasNode() {
		return arasNode;
	}
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(final Association assoc) {
		handler.addAssociation(this, assoc);
		logger.debug("Added Association: " + assoc);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(final Association assoc) {
		super.remove(assoc);
		logger.debug("Removed Association: " + assoc);
		return handler.removeAssociation(this, assoc);
	}
	
	// ----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public Set<Association> getAssociationsForRemoval() {
		return Collections.emptySet();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAttached() {
		return true;
	}
	
	// ----------------------------------------------------
	
	/**
	 * Add the association directly to the associations.
	 */
	public void addAssociationDirectly(final Association assoc) {
		super.add(assoc);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void resolveAssociations() {
		handler.resolveAssociations(this);
	}
	
	// ----------------------------------------------------
	
	/**
	 * Called when being serialized --> Replace by detached association keeper.
	 * @return A Detached Association Keeper.
	 */
	private Object writeReplace() {
		return new DetachedAssociationKeeper(getAssociationsDirectly());
	}

}
