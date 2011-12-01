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
import java.util.HashSet;
import java.util.Set;

import org.arastreju.bindings.neo4j.ArasRelTypes;
import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.bindings.neo4j.mapping.NodeMapper;
import org.arastreju.bindings.neo4j.tx.TxAction;
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.bindings.neo4j.tx.TxResultAction;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class SemanticNetworkAccess implements NeoConstants, NeoResourceResolver {
	
	private final GraphDatabaseService gdbService;
	
	private final ResourceIndex index;
	
	private final NodeMapper mapper;
	
	private final AssociationHandler assocHandler;
	
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
		this.txProvider = new TxProvider(gdbService);
		this.mapper = new NodeMapper(this);
		this.index = new ResourceIndex(mapper, store.getIndexManager(), txProvider);
		this.assocHandler = new AssociationHandler(gdbService, this, txProvider, index);
	}
	
	// -----------------------------------------------------
	
	public ResourceIndex getIndex() {
		return index;
	}
	
	public GraphDatabaseService getGdbService() {
		return gdbService;
	}
	
	public TxProvider getTxProvider() {
		return txProvider;
	}

	// -- FIND / RESOLVE ----------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode findResource(final QualifiedName qn) {
		return index.findResource(qn);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode resolve(final ResourceID resource) {
		final ResourceNode node = resource.asResource();
		if (node.isAttached()){
			return node;
		} else {
			final ResourceNode attached = findResource(resource.getQualifiedName());
			if (attached != null) {
				return attached;
			} else {
				return txProvider.doTransacted(new TxResultAction<ResourceNode>() {
					public ResourceNode execute() {
						return persist(resource.asResource(), true);
					}
				});
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode resolve(final Node neoNode) {
		return index.resolveResource(neoNode);
	}
	
	// -----------------------------------------------------
	
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
		return txProvider.doTransacted(new TxResultAction<ResourceNode>() {
			public ResourceNode execute() {
				// 2nd: check if node for qualified name exists and has to be merged
				ResourceNode attached = findResource(resource.getQualifiedName());
				if (attached != null){
					attached = merge(attached, resource);
				} else {
					// 3rd: if resource is really new, create a new Neo node.
					attached = persist(resource, true);
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
		index.removeFromRegister(node);
		AssocKeeperAccess.setAssociationKeeper(node, new DetachedAssociationKeeper(node.getAssociations()));
	}
	
	/**
	 * Remove the node.
	 * @param id The ID.
	 * @param cascade The cascade flag.
	 */
	public void remove(final ResourceID id, final boolean cascade) {
		final ResourceNode node = resolve(id);
		AssocKeeperAccess.getAssociationKeeper(node).clearAssociations();
		txProvider.doTransacted(new TxAction() {
			public void execute() {
				new NodeRemover(index).remove(node, cascade);
			}
		});
	}
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public SemanticGraph attach(final SemanticGraph graph){
		return txProvider.doTransacted(new TxResultAction<SemanticGraph>() {
			public SemanticGraph execute() {
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
			if (node.isResourceNode() && node.asResource().isAttached()){
				detach(node.asResource());
			}
		}
	}
	
	// -----------------------------------------------------
	
	/**
	 * Close the graph database;
	 */
	public void close() {
		index.clearRegister();
	}

	/**
	 * Add a new Association to given Neo node, or rather create a corresponding Relation.
	 * @param subject The neo node, which shall be the subject in the new Relation.
	 * @param stmt The Association.
	 */
	public void addAssociation(final Node subject, final Statement... statements) {
		final Set<Statement> inferenced = new HashSet<Statement>();
		for (Statement stmt : statements) {
			new NeoInferencer().addInferenced(stmt, inferenced);
			inferenced.add(stmt);
		}
		txProvider.doTransacted(new TxAction() {
			public void execute() {
				createRelationships(subject, inferenced);
			}
		});
	}
	
	/**
	 * Remove the association from the neo node.
	 * @param neoNode The neo node.
	 * @param assoc The corresponding association.
	 * @return true if a relationship has been removed.
	 */
	public boolean removeAssociation(final Node neoNode, final Statement assoc) {
		final Relationship relationship = findCorresponding(neoNode, assoc);
		if (relationship != null) {
			txProvider.doTransacted(new TxAction() {
				public void execute() {
					index.removeFromIndex(relationship);
					logger.warn("Deleting: " + assoc);
					relationship.delete();
				}
			});
			return true;
		} else {
			logger.warn("Didn't find corresponding relationship to delete: " + assoc);
			return false;	
		}
	}
	
	// -----------------------------------------------------
	
	/**
	 * Create the given resource node in Neo4j DB.
	 * @param node A not yet persisted node.
	 * @param storeAssocs Flag indicating, if assocs of the node shall be persisted to.
	 * @return The persisted ResourceNode.
	 */
	protected ResourceNode persist(final ResourceNode node, boolean storeAssocs) {
		// 1st: create a corresponding Neo node.
		final Node neoNode = gdbService.createNode();
		mapper.toNeoNode(node, neoNode);
		
		// 2nd: index the Neo node.
		index.index(neoNode, node);
		
		// 3rd: attach the Resource with this store.
		final Set<Association> copy = node.getAssociations();
		final NeoAssociationKeeper keeper = new NeoAssociationKeeper(node, neoNode, this);
		AssocKeeperAccess.setAssociationKeeper(node, keeper);
		
		// 4th: store all associations.
		if (storeAssocs) {
			for (Association assoc : copy) {
				keeper.add(assoc);
			}
		}
		
		return node;
	}
	
	/**
	 * Merges all associations from the 'changed' node to the 'attached' node.
	 * @param attached The currently attached node.
	 * @param changed An unattached node referencing the same resource.
	 * @return The merged {@link ResourceNode}.
	 */
	protected ResourceNode merge(final ResourceNode attached, final ResourceNode changed) {
		final AssociationKeeper ak = AssocKeeperAccess.getAssociationKeeper(changed);
		for (Association toBeRemoved : ak.getAssociationsForRemoval()) {
			attached.remove(toBeRemoved);
		}
		final Set<Association> currentAssocs = attached.getAssociations();
		for(Association assoc : ak.getAssociations()){
			if (!currentAssocs.contains(assoc)){
				Association.create(attached, assoc.getPredicate(), assoc.getObject(), assoc.getContexts());
			}
		}
		return attached;
	}
	

	/**
	 * Assigns context information to a relationship.
	 * @param relationship The relationship to be assigned to the contexts.
	 * @param contexts The contexts.
	 */
	protected void assignContext(final Relationship relationship, final Context[] contexts) {
		new ContextAccess(this).assignContext(relationship, contexts);
	}
	
	protected Relationship findCorresponding(final Node neoNode, final Statement stmt) {
		final String assocPredicate = stmt.getPredicate().getQualifiedName().toURI();
		final String assocValue = SNOPS.string(stmt.getObject());
		for(Relationship rel : neoNode.getRelationships(Direction.OUTGOING)) {
			final String predicate = (String) rel.getProperty(PREDICATE_URI);
			if (assocPredicate.equals(predicate)) {
				if (stmt.getObject().isResourceNode()) {
					final String uri = (String) rel.getEndNode().getProperty(PROPERTY_URI);
					if (assocValue.equals(uri)) {
						return rel;
					}
				} else {
					final String value = (String) rel.getEndNode().getProperty(PROPERTY_VALUE);
					if (assocValue.equals(value)) {
						return rel;
					}
				}
			}
		}
		return null;
	}
	
	private void createRelationships(Node subject, Set<Statement> statments) {
		for (Statement stmt : statments) {
			resolve(stmt.getPredicate());
			if (stmt.getObject().isResourceNode()){
				final ResourceNode arasClient = resolve(stmt.getObject().asResource());
				final Node neoClient = AssocKeeperAccess.getNeoNode(arasClient);
				createRelationShip(subject, neoClient, stmt);
			} else {
				final Node neoClient = gdbService.createNode();
				mapper.toNeoNode(stmt.getObject().asValue(), neoClient);
				createRelationShip(subject, neoClient, stmt);
			}
			index.index(subject, stmt);
		}
	}
	
	private void createRelationShip(final Node subject, final Node object, final Statement stmt) {
		final RelationshipType type = stmt.getObject().isResourceNode() ? ArasRelTypes.REFERENCE : ArasRelTypes.VALUE;
		final Relationship relationship = subject.createRelationshipTo(object, type);
		relationship.setProperty(PREDICATE_URI, SNOPS.uri(stmt.getPredicate()));
		assignContext(relationship, stmt.getContexts());
		logger.debug("added relationship--> " + relationship + " to node " + subject);
	}

}
