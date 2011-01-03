/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import org.arastreju.bindings.neo4j.impl.Neo4jDataStore;
import org.arastreju.bindings.neo4j.impl.NodeRegistry;
import org.arastreju.sge.ModellingConversation;
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
 *  Implementation of {@link ModellingConversation} for Neo4j.
 * </p>
 *
 * <p>
 * 	Created Sep 2, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class Neo4jModellingConversation implements ModellingConversation {
	
	private final Neo4jDataStore store;
	
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(Neo4jModellingConversation.class);
	
	// -----------------------------------------------------

	/**
	 * Create a new Modelling Conversation instance using a given data store.
	 */
	public Neo4jModellingConversation(final Neo4jDataStore graphDb) {
		this.store = graphDb;
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
		throw new NotYetImplementedException();
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
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#detach(org.arastreju.sge.model.SemanticGraph)
	 */
	public void detach(final SemanticGraph graph) {
		throw new NotYetImplementedException();
	}

	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#createQueryManager()
	 */
	public QueryManager createQueryManager() {
		throw new NotYetImplementedException();
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#commit()
	 */
	public void commit() {
		// TODO Auto-generated method stub
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#flush()
	 */
	public void flush() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#getSettings()
	 */
	public ConversationSettings getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#getTransactionControl()
	 */
	public TransactionControl getTransactionControl() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#rollback()
	 */
	public void rollback() {
		// TODO Auto-generated method stub
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#close()
	 */
	public void close() {
		store.close();
	}
	
	// -----------------------------------------------------
	
	protected NodeRegistry getRegistry() {
		return store.getRegistry();
	}
	
}
