/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import java.io.File;
import java.io.IOException;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.bindings.neo4j.mapping.NodeMapper;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneIndexService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

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
public class Neo4jDataStore implements NeoConstants {
	
	private final GraphDatabaseService gdbService;
	
	private final IndexService indexService;
	
	private final NodeMapper mapper;
	
	private final NodeRegistry registry = new NodeRegistry();
	
	private final Logger logger = LoggerFactory.getLogger(Neo4jDataStore.class);

	// -----------------------------------------------------

	public Neo4jDataStore() throws IOException {
		this(prepareTempStore());
	}
	
	public Neo4jDataStore(final String dir) {
		logger.info("Neo4jDataStore created in " + dir);
		gdbService = new EmbeddedGraphDatabase(dir); 
		indexService = new LuceneIndexService(gdbService);
		mapper = new NodeMapper(this);
	}

	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#findResource(com.sun.xml.internal.fastinfoset.QualifiedName)
	 */
	public ResourceNode findResource(final QualifiedName qn) {
		if (registry.contains(qn)){
			return registry.get(qn);
		}
		// if not yet registered, load and wrap
		return doTransacted(new TxResultAction<ResourceNode>() {
			public ResourceNode execute(Neo4jDataStore store) {
				final Node neoNode = indexService.getSingleNode(INDEX_KEY_RESOURCE_URI, qn.toURI());
				logger.info("IndexLookup: " + qn + " --> " + neoNode); 
				if (neoNode != null){
					if (neoNode.getRelationships().iterator().hasNext()){
						logger.info(" -- relationships--> " + neoNode.getRelationships().iterator().next());
					} else {
						logger.info(" -- no relationships");
					}
					final SNResource arasNode = mapper.toArasNode(neoNode);
					registry.register(arasNode);
					return arasNode;
				} else {
					return null;
				}
			}
		});
	}
	
	public ResourceNode findResource(final Node neoNode) {
		final QualifiedName qn = new QualifiedName(neoNode.getProperty(PROPERTY_URI).toString());
		if (registry.contains(qn)){
			return registry.get(qn);
		}
		final SNResource arasNode = mapper.toArasNode(neoNode);
		registry.register(arasNode);
		return arasNode;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#resolve(org.arastreju.sge.model.ResourceID)
	 */
	public ResourceNode resolve(final ResourceID resource) {
		if (resource.isAttached()){
			return resource.asResource();
		} else {
			ResourceNode node = findResource(resource.getQualifiedName());
			if (node == null){
				node = persist(resource.asResource());
			}
			return node;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#attach(org.arastreju.sge.model.nodes.ResourceNode)
	 */
	public ResourceNode attach(final ResourceNode node) {
		if (node.isAttached()){
			return node;
		}
		return doTransacted(new TxResultAction<ResourceNode>() {
			public ResourceNode execute(Neo4jDataStore store) {
				// 1st: check if node does already exist and has to be merged
				ResourceNode attached = findResource(node.getQualifiedName());
				if (attached != null){
					attached = merge(attached, node);
				} else {
					attached = persist(node);
				}
				return attached;
			}
		});
	}
	
	/**
	 * Create the given resource node in neo4j db.
	 * @param node A not yet persisted node.
	 * @return The persisted ResourceNode.
	 */
	public ResourceNode persist(final ResourceNode node) {
		final Node neoNode = gdbService.createNode();
		mapper.toNeoNode(node, neoNode);
		final QualifiedName qn = node.getQualifiedName();
		indexService.index(neoNode, INDEX_KEY_RESOURCE_URI, qn.toURI());
		logger.info("Indexed: " + qn + " --> " + neoNode);
		
		final Iterable<Relationship> relationships = neoNode.getRelationships(Direction.OUTGOING);
		for (Relationship rel : relationships) {
			rel.getEndNode();
		}
		
		AssocKeeperAccess.setAssociationKeeper(node, new NeoAssociationKeeper(neoNode, this));
		
		return node;
	}
	
	public void detach(final ResourceNode node){
		AssocKeeperAccess.setAssociationKeeper(node, new DetachedAssociationKeeper(node.getAssociations()));
	}
	
	public void createAssociation(final Association assoc) {
		assoc.getClient();
	}

	// -----------------------------------------------------
	
	/**
	 * Close the graph database;
	 */
	public void close() {
		gdbService.shutdown();
	}

	public void addAssociation(final Node subject, final Association assoc) {
		doTransacted(new TxAction() {
			public void execute(Neo4jDataStore store) {
				final SemanticNode client = assoc.getClient();
				final ResourceNode predicate = resolve(assoc.getPredicate());
				
				if (client.isResourceNode()){
					final ResourceNode arasClient = resolve(client.asResource());
					final Node neoClient = AssocKeeperAccess.getNeoNode(arasClient);
					
					Relationship relationship = subject.createRelationshipTo(neoClient, 
							DynamicRelationshipType.withName( "KNOWS" ));
					relationship.setProperty(PROPERTY_URI, predicate.getQualifiedName().toURI());
					logger.info("added relationship--> " + relationship + " to node " + subject);
				}
			}
		});
	}
	
	// -----------------------------------------------------
	
	public void doTransacted(final TxAction action){
		Transaction tx = gdbService.beginTx();
		try {
			action.execute(this);
			tx.success();
		} finally {
			tx.finish();
		}
	}
	
	public <T> T doTransacted(final TxResultAction<T> action){
		Transaction tx = gdbService.beginTx();
		try {
			T result = action.execute(this);
			tx.success();
			return result;
		} finally {
			tx.finish();
		}
	}
	
	// -----------------------------------------------------
	
	/**
	 * @param attached The currently attached node.
	 * @param node An unattached node referencing the same resource.
	 * @return The merged {@link ResourceNode}.
	 */
	public ResourceNode merge(final ResourceNode attached, final ResourceNode node) {
		throw new NotYetImplementedException();
	}
	
	public IndexService getIndexService() {
		return indexService;
	}
	
	public GraphDatabaseService getGdbService() {
		return gdbService;
	}
	
	public NodeRegistry getRegistry() {
		return registry;
	}
	
	// -----------------------------------------------------
	
	private static String prepareTempStore() throws IOException {
		final File temp = File.createTempFile("aras", Long.toString(System.nanoTime()));
		if (!temp.delete()) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}
		if (!temp.mkdir()) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}

		return temp.getAbsolutePath();
	}

}
