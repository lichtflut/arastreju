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
package org.arastreju.bindings.neo4j;

import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.bindings.neo4j.impl.ResourceRegistry;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.query.QueryManager;
import org.arastreju.sge.settings.ConversationSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

/**
 * <p>
 *  Implementation of {@link ModelingConversation} for Neo4j.
 * </p>
 *
 * <p>
 * 	Created Sep 2, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class Neo4jModellingConversation implements ModelingConversation {
	
	private final SemanticNetworkAccess store;
	
	private final TransactionControl txc;
	
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(Neo4jModellingConversation.class);
	
	// -----------------------------------------------------

	/**
	 * Create a new Modelling Conversation instance using a given data store.
	 */
	public Neo4jModellingConversation(final SemanticNetworkAccess graphDb) {
		this.store = graphDb;
		this.txc = graphDb.getTransactionControl();
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#findResource(com.sun.xml.internal.fastinfoset.QualifiedName)
	 */
	public ResourceNode findResource(final QualifiedName qn) {
		return store.findResource(qn);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#resolve(org.arastreju.sge.model.ResourceID)
	 */
	public ResourceNode resolve(final ResourceID resource) {
		return store.resolve(resource);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#attach(org.arastreju.sge.model.nodes.ResourceNode)
	 */
	public ResourceNode attach(final ResourceNode node) {
		return store.attach(node);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#detach(org.arastreju.sge.model.nodes.ResourceNode)
	 */
	public void detach(final ResourceNode node) {
		store.detach(node);
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#findGraph(com.sun.xml.internal.fastinfoset.QualifiedName)
	 */
	public SemanticGraph findGraph(final QualifiedName qn) {
		throw new NotYetImplementedException();
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#attach(org.arastreju.sge.model.SemanticGraph)
	 */
	public SemanticGraph attach(final SemanticGraph graph) {
		return store.attach(graph);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#detach(org.arastreju.sge.model.SemanticGraph)
	 */
	public void detach(final SemanticGraph graph) {
		store.detach(graph);
	}

	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#createQueryManager()
	 */
	public QueryManager createQueryManager() {
		return new NeoQueryManager(store);
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#getSettings()
	 */
	public ConversationSettings getSettings() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#getTransactionControl()
	 */
	public TransactionControl getTransactionControl() {
		return txc;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#close()
	 */
	public void close() {
		store.close();
	}
	
	// -----------------------------------------------------
	
	protected ResourceRegistry getRegistry() {
		return store.getRegistry();
	}
	
}
