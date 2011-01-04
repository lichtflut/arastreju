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
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;
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
 *  The Neo4jDataStore consists of three data containers:
 *  <ul>
 *  	<li>The Graph Database Service, containing the actual graph</li>
 *  	<li>An Index Service, mapping URLs and keywords to nodes</li>
 *  	<li>A Registry mapping QualifiedNames to Arastreju Resources</li>
 *  </ul>
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
	
	private final ResourceRegistry registry = new ResourceRegistry();
	
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
	
	/**
	 * Attach the given node if it is not already attached.
	 * @param resource The node to attach.
	 * @return A node attached by guaranty.
	 */
	public ResourceNode attach(final ResourceNode resource) {
		// 1st: check if node is already attached.
		if (resource.isAttached()){
			return resource;
		}
		return doTransacted(new TxResultAction<ResourceNode>() {
			public ResourceNode execute(Neo4jDataStore store) {
				// 2nd: check if node for qualified name exists and has to be merged
				ResourceNode attached = findResource(resource.getQualifiedName());
				if (attached != null){
					attached = merge(attached, resource);
				} else {
					// 3rd: if resource is really new, create a new Neo node.
					attached = persist(resource);
				}
				return attached;
			}
		});
	}
	
	/**
	 * Unregister the node from the registry and detach the {@link AssociationKeeper}
	 * @param node
	 */
	public void detach(final ResourceNode node){
		registry.unregister(node);
		AssocKeeperAccess.setAssociationKeeper(node, new DetachedAssociationKeeper(node.getAssociations()));
	}
	
	// -----------------------------------------------------
	
	/**
	 * Close the graph database;
	 */
	public void close() {
		gdbService.shutdown();
	}

	/**
	 * Add a new Association to given Neo node, or rather create a corresponding Relation.
	 * @param subject The neo node, which shall be the subject in the new Relation.
	 * @param assoc The Association.
	 */
	public void addAssociation(final Node subject, final Association assoc) {
		doTransacted(new TxAction() {
			public void execute(Neo4jDataStore store) {
				final SemanticNode client = assoc.getClient();
				final ResourceNode predicate = resolve(assoc.getPredicate());
				
				if (client.isResourceNode()){
					final ResourceNode arasClient = resolve(client.asResource());
					final Node neoClient = AssocKeeperAccess.getNeoNode(arasClient);
					
					final Relationship relationship = subject.createRelationshipTo(neoClient, 
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
	 * @param changed An unattached node referencing the same resource.
	 * @return The merged {@link ResourceNode}.
	 */
	public ResourceNode merge(final ResourceNode attached, final ResourceNode changed) {
		throw new NotYetImplementedException();
	}
	
	public IndexService getIndexService() {
		return indexService;
	}
	
	public GraphDatabaseService getGdbService() {
		return gdbService;
	}
	
	public ResourceRegistry getRegistry() {
		return registry;
	}
	
	// -----------------------------------------------------
	
	/**
	 * Create the given resource node in Neo4j DB.
	 * @param node A not yet persisted node.
	 * @return The persisted ResourceNode.
	 */
	protected ResourceNode persist(final ResourceNode node) {
		// 1st: create a corresponding Neo node.
		final Node neoNode = gdbService.createNode();
		mapper.toNeoNode(node, neoNode);
		
		// 2nd: index the Neo node.
		final QualifiedName qn = node.getQualifiedName();
		indexService.index(neoNode, INDEX_KEY_RESOURCE_URI, qn.toURI());
		logger.debug("Indexed: " + qn + " --> " + neoNode);
		
		// 3rd: register resource.
		registry.register(node);
		
		// 4th: attach the Resource with this store.
		AssocKeeperAccess.setAssociationKeeper(node, new NeoAssociationKeeper(neoNode, this));
		
		return node;
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
