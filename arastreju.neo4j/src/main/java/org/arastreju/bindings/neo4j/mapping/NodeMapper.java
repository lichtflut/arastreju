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
package org.arastreju.bindings.neo4j.mapping;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.bindings.neo4j.extensions.SNValueNeo;
import org.arastreju.bindings.neo4j.impl.AssocKeeperAccess;
import org.arastreju.bindings.neo4j.impl.ContextAccess;
import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Sep 2, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class NodeMapper implements NeoConstants {
	
	private final SemanticNetworkAccess store;

	/**
	 * Default constructor.
	 * @param neo4jDataStore 
	 */
	public NodeMapper(SemanticNetworkAccess neo4jDataStore) {
		this.store = neo4jDataStore;
	}
	
	// -----------------------------------------------------

	public void toNeoNode(final ResourceNode arasNode, final Node neoNode){
		neoNode.setProperty(PROPERTY_URI, arasNode.getQualifiedName().toURI());
	}
	
	public void toArasNode(final Node neoNode, final SNResource arasNode){
		final NeoAssociationKeeper assocKeeper = new NeoAssociationKeeper(arasNode, neoNode, store);
		
		AssocKeeperAccess.setAssociationKeeper(arasNode, assocKeeper);
		
		for(Relationship rel : neoNode.getRelationships(Direction.OUTGOING)){
			final Node neoClient = rel.getEndNode();
			final Context[] ctx = new ContextAccess(store).getContextInfo(rel);
			if (neoClient.hasProperty(PROPERTY_URI)){
				// Resource Relation
				ResourceNode object = store.findResource(rel.getEndNode());
				ResourceNode predicate = store.findResource(new QualifiedName(rel.getProperty(PREDICATE_URI).toString()));
				assocKeeper.addResolvedAssociation(arasNode, predicate, object,ctx);
			} else if (neoClient.hasProperty(PROPERTY_VALUE)){
				// Value assignment
				final SNValue value = new SNValueNeo(neoClient);
				ResourceNode predicate = store.findResource(new QualifiedName(rel.getProperty(PREDICATE_URI).toString()));
				assocKeeper.addResolvedAssociation(arasNode, predicate, value,ctx);
			} else {
				throw new IllegalStateException("Relation end has neither URI nor Value");
			}
		}
		
		assocKeeper.markResolved();
	}
	
}
