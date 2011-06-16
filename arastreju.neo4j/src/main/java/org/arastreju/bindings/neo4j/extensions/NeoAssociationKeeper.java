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

import org.arastreju.bindings.neo4j.ArasRelTypes;
import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.impl.ContextAccess;
import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.AbstractAssociationKeeper;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
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
	private final SemanticNetworkAccess store;
	
	private final Logger logger = LoggerFactory.getLogger(NeoAssociationKeeper.class);
	
	// -----------------------------------------------------
	
	/**
	 * Create a new association keeper.
	 * @param arasNode The aras node
	 * @param neoNode The neo node.
	 */
	public NeoAssociationKeeper(final ResourceNode arasNode, final Node neoNode, final SemanticNetworkAccess store) {
		this.arasNode = arasNode;
		this.neoNode = neoNode;
		this.store = store;
	}
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public boolean isAttached() {
		return true;
	}
	
	@Override
	public void add(final Association assoc) {
		super.add(assoc);
		logger.info("Added Association: " + assoc);
		store.addAssociation(neoNode, assoc);
	}
	
	// -----------------------------------------------------
	
	/**
	 * @return the neoNode
	 */
	public Node getNeoNode() {
		return neoNode;
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.associations.AbstractAssociationKeeper#markResolved()
	 */
	@Override
	public AbstractAssociationKeeper markResolved() {
		return super.markResolved();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Set<Association> getAssociationsForRemoval() {
		return Collections.emptySet();
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.associations.AbstractAssociationKeeper#addResolvedAssociation(org.arastreju.sge.model.nodes.ResourceNode, org.arastreju.sge.model.ResourceID, org.arastreju.sge.model.nodes.SemanticNode, org.arastreju.sge.context.Context[])
	 */
	@Override
	public void addResolvedAssociation(ResourceNode subject,
			ResourceID predicate, SemanticNode object, Context... contexts) {
		super.addResolvedAssociation(subject, predicate, object, contexts);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.associations.AbstractAssociationKeeper#resolveAssociations()
	 */
	@Override
	protected void resolveAssociations() {
		for(Relationship rel : neoNode.getRelationships(Direction.OUTGOING)){
			SemanticNode object = null;
			if (rel.isType(ArasRelTypes.REFERENCE)){
				object = store.findResource(rel.getEndNode());	
			} else if (rel.isType(ArasRelTypes.VALUE)){
				object = new SNValueNeo(rel.getEndNode());
			}
			final ResourceNode predicate = store.findResource(new QualifiedName(rel.getProperty(PREDICATE_URI).toString()));
			final Context[] ctx = new ContextAccess(store).getContextInfo(rel);
			addResolvedAssociation(arasNode, predicate, object, ctx);
		}
	}
	
	/**
	 * Called when beeing serialized --> Replace by detached assoc keeper
	 * @return A Detached Association Keeper.
	 */
	private Object writeReplace() {
		logger.info("Serializing NeoAssociationKeeper --> Detaching");
		return new DetachedAssociationKeeper(getAssociationsDirectly());
	}
	
}
