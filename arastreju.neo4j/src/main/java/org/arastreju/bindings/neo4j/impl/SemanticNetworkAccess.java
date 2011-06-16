/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.bindings.neo4j.impl;

import java.io.IOException;
import java.util.Set;

import org.arastreju.bindings.neo4j.ArasRelTypes;
import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.bindings.neo4j.extensions.SNResourceNeo;
import org.arastreju.bindings.neo4j.index.NeoIndexingService;
import org.arastreju.bindings.neo4j.mapping.NodeMapper;
import org.arastreju.bindings.neo4j.tx.NeoTransactionControl;
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.bindings.neo4j.tx.TxWrapper;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.index.IndexService;
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
public class SemanticNetworkAccess implements NeoConstants, ResourceResolver {
	
	private final GraphDatabaseService gdbService;
	
	private final NeoIndexingService indexService;
	
	private final NodeMapper mapper;
	
	private final ResourceRegistry registry;
	
	private final TxProvider txProvider;
	
	private final Logger logger = LoggerFactory.getLogger(SemanticNetworkAccess.class);

	// -----------------------------------------------------

	/**
	 * Default constructor. Will use a <b>temporary</b> datastore!.
	 */
	public SemanticNetworkAccess() throws IOException {
		this(new GraphDataStore());
	}
	
	/**
	 * Constructor. Creates a store using given directory.
	 * @param dir The directory for the store.
	 */
	public SemanticNetworkAccess(final String dir) {
		this(new GraphDataStore(dir));
	}
	
	/**
	 * Constructor. Creates a store using given directory.
	 * @param dir The directory for the store.
	 */
	public SemanticNetworkAccess(final GraphDataStore store) {
		this.gdbService = store.getGdbService();
		this.indexService = store.getIndexService();
		this.registry = store.getRegistry();
		
		this.txProvider = new TxProvider(gdbService);
		this.mapper = new NodeMapper(this);
	}

	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode findResource(final QualifiedName qn) {
		if (registry.contains(qn)){
			return registry.get(qn);
		}
		// if not yet registered, load and wrap
		return doTransacted(new TxResultAction<ResourceNode>() {
			public ResourceNode execute(SemanticNetworkAccess store) {
				final Node neoNode = indexService.getSingleNode(INDEX_KEY_RESOURCE_URI, qn.toURI());
				logger.debug("IndexLookup: " + qn + " --> " + neoNode); 
				if (neoNode != null){
					final SNResourceNeo arasNode = new SNResourceNeo(qn);
					registry.register(arasNode);
					mapper.toArasNode(neoNode, arasNode);
					return arasNode;
				} else {
					return null;
				}
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode findResource(final Node neoNode) {
		final QualifiedName qn = new QualifiedName(neoNode.getProperty(PROPERTY_URI).toString());
		if (registry.contains(qn)){
			return registry.get(qn);
		}
		final SNResourceNeo arasNode = new SNResourceNeo(qn);
		registry.register(arasNode);
		mapper.toArasNode(neoNode, arasNode);
		return arasNode;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode resolve(final ResourceID resource) {
		if (resource.isAttached()){
			return resource.asResource();
		} else {
			ResourceNode node = findResource(resource.getQualifiedName());
			if (node == null){
				return doTransacted(new TxResultAction<ResourceNode>() {
					public ResourceNode execute(SemanticNetworkAccess store) {
						return persist(resource.asResource());
					}
				});
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
			public ResourceNode execute(SemanticNetworkAccess store) {
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
	 * {@inheritDoc}
	 */
	public SemanticGraph attach(final SemanticGraph graph){
		return doTransacted(new TxResultAction<SemanticGraph>() {
			public SemanticGraph execute(SemanticNetworkAccess store) {
				for(ResourceNode node : graph.getSubjects()){
					attach(node);
				}
				return graph;
			}
		});
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void detach(final SemanticGraph graph){
		for(SemanticNode node : graph.getNodes()){
			if (node.isAttached() && node.isResourceNode()){
				detach(node.asResource());
			}
		}
	}
	
	// -----------------------------------------------------
	
	/**
	 * Close the graph database;
	 */
	public void close() {
	}

	/**
	 * Add a new Association to given Neo node, or rather create a corresponding Relation.
	 * @param subject The neo node, which shall be the subject in the new Relation.
	 * @param assoc The Association.
	 */
	public void addAssociation(final Node subject, final Association assoc) {
		doTransacted(new TxAction() {
			public void execute(SemanticNetworkAccess store) {
				final SemanticNode client = assoc.getObject();
				final ResourceNode predicate = resolve(assoc.getPredicate());
				if (client.isResourceNode()){
					// Resource node
					final ResourceNode arasClient = resolve(client.asResource());
					final Node neoClient = AssocKeeperAccess.getNeoNode(arasClient);
					
					final Relationship relationship = subject.createRelationshipTo(neoClient, ArasRelTypes.REFERENCE);
					relationship.setProperty(PREDICATE_URI, predicate.getQualifiedName().toURI());
					assignContext(relationship, assoc.getContexts());
					indexService.index(subject, predicate.getQualifiedName().toURI(), arasClient.getQualifiedName().toURI());
					logger.debug("added relationship--> " + relationship + " to node " + subject);
				} else {
					// Value node
					final Node neoClient = gdbService.createNode();
					final ValueNode value = client.asValue();
					neoClient.setProperty(PROPERTY_VALUE, value.getValue().toString());
					neoClient.setProperty(PROPERTY_DATATYPE, client.asValue().getDataType().name());
					
					final Relationship relationship = subject.createRelationshipTo(neoClient, ArasRelTypes.VALUE);
					relationship.setProperty(PREDICATE_URI, predicate.getQualifiedName().toURI());
					assignContext(relationship, assoc.getContexts());
					
					logger.debug("added value --> " + relationship + " to node " + subject);

					indexService.index(subject, INDEX_KEY_RESOURCE_VALUE, value.getStringValue());
					indexService.index(subject, predicate.getQualifiedName().toURI(), value.getStringValue());
					logger.debug("Indexed: " + value.getStringValue() + " --> " + subject);
				}
			}
		});
	}
	

	/**
	 * Merges all associations from the 'changed' node to the 'attached' node.
	 * @param attached The currently attached node.
	 * @param changed An unattached node referencing the same resource.
	 * @return The merged {@link ResourceNode}.
	 */
	public ResourceNode merge(final ResourceNode attached, final ResourceNode changed) {
		final AssociationKeeper ak = AssocKeeperAccess.getAssociationKeeper(changed);
		for (Association toBeRemoved : ak.getAssociationsForRemoval()) {
			attached.remove(toBeRemoved);
		}
		if (!ak.getAssociationsForRevocation().isEmpty()){
			throw new NotYetImplementedException("Revoked Assocs cannot be merged yet.");
		}
		final Set<Association> currentAssocs = attached.getAssociations();
		for(Association assoc : ak.getAssociations()){
			if (!currentAssocs.contains(assoc)){
				Association.create(attached, assoc.getPredicate(), assoc.getObject(), assoc.getContexts());
			}
		}
		return attached;
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
	
	// -- TRANSACTIONS ------------------------------------
	
	/**
	 * @return the transaction control.
	 */
	public NeoTransactionControl getTransactionControl() {
		return new NeoTransactionControl(txProvider);
	}
	
	public void doTransacted(final TxAction action){
		final TxWrapper tx = txProvider.begin();
		try {
			action.execute(this);
			tx.markSuccessful();
		} finally {
			tx.finish();
		}
	}
	
	public <T> T doTransacted(final TxResultAction<T> action){
		final TxWrapper tx = txProvider.begin();
		try {
			T result = action.execute(this);
			tx.markSuccessful();
			return result;
		} finally {
			tx.finish();
		}
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
		
		// 3rd: attach the Resource with this store.
		final Set<Association> copy = node.getAssociations();
		AssocKeeperAccess.setAssociationKeeper(node, new NeoAssociationKeeper(node, neoNode, this));
		
		// 4th: register resource.
		registry.register(node);
		
		// 5th: store all associations.
		for (Association assoc : copy) {
			addAssociation(neoNode, assoc);
		}
		
		return node;
	}
	

	/**
	 * Assigns context information to a relationship.
	 * @param relationship The relationship to be assigned to the contexts.
	 * @param contexts The contexts.
	 */
	protected void assignContext(final Relationship relationship, final Context[] contexts) {
		new ContextAccess(this).assignContext(relationship, contexts);
	}
	
}
