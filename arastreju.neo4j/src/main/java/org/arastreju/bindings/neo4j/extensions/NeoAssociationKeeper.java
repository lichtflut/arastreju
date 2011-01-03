/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.extensions;

import org.arastreju.bindings.neo4j.impl.Neo4jDataStore;
import org.arastreju.sge.model.associations.AbstractAssociationKeeper;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.associations.AssociationKeeper;
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
public class NeoAssociationKeeper extends AbstractAssociationKeeper {
	
	private final Node neoNode;
	private final Neo4jDataStore store;
	
	private final Logger logger = LoggerFactory.getLogger(NeoAssociationKeeper.class);
	
	// -----------------------------------------------------
	
	/**
	 * Create a new association keeper.
	 * @param neoNode The neo node.
	 */
	public NeoAssociationKeeper(final Node neoNode, final Neo4jDataStore store) {
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
	 * @see org.arastreju.sge.model.associations.AbstractAssociationKeeper#resolveAssociations()
	 */
	@Override
	protected void resolveAssociations() {
		logger.warn("Not yet implemented: resolveAssociations()");
		
//		for(Relationship rel : neoNode.getRelationships(Direction.OUTGOING)){
//			ResourceNode object = store.findResource(rel.getEndNode());
//			ResourceNode predicate = store.findResource(new QualifiedName(rel.getProperty(PROPERTY_URI).toString()));
//			
//			Association.create(arasNode, predicate, object, null);
//		}
	}

}
