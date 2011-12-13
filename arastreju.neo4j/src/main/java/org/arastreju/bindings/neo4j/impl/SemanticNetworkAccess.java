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

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.bindings.neo4j.extensions.SNResourceNeo;
import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.bindings.neo4j.tx.TxAction;
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.bindings.neo4j.tx.TxResultAction;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

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
	
	private final AssociationHandler assocHandler;
	
	private final TxProvider txProvider;
	
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
		this.index = new ResourceIndex(this, store.getIndexManager(), txProvider);
		this.assocHandler = new AssociationHandler(this, index, txProvider);
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
		final ResourceNode registered = index.findResourceNode(qn);
		if (registered != null) {
			return registered;
		}
		final Node neoNode = index.findNeoNode(qn);
		if (neoNode != null){
			return createArasNode(neoNode, qn);
		} else {
			return null;
		}
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
						return persist(resource.asResource());
					}
				});
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ResourceNode resolve(final Node neoNode) {
		final QualifiedName qn = new QualifiedName(neoNode.getProperty(PROPERTY_URI).toString());
		final ResourceNode found = index.findResourceNode(qn);
		if (found != null){
			return found;
		}
		return createArasNode(neoNode, qn);
	}

	protected SNResourceNeo createArasNode(final Node neoNode, final QualifiedName qn) {
		final SNResourceNeo arasNode = new SNResourceNeo(qn);
		index.register(arasNode);
		final NeoAssociationKeeper assocKeeper = new NeoAssociationKeeper(arasNode, neoNode, assocHandler);
		AssocKeeperAccess.setAssociationKeeper(arasNode, assocKeeper);
		return arasNode;
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
				for(Statement stmt : graph.getStatements()) {
					final ResourceNode subject = resolve(stmt.getSubject());
					SNOPS.associate(subject, stmt.getPredicate(), stmt.getObject(), stmt.getContexts());
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

	// -----------------------------------------------------
	
	/**
	 * Create the given resource node in Neo4j DB.
	 * @param node A not yet persisted node.
	 * @return The persisted ResourceNode.
	 */
	protected ResourceNode persist(final ResourceNode node) {
		// 1st: create a corresponding Neo node.
		final Node neoNode = gdbService.createNode();
		neoNode.setProperty(PROPERTY_URI, node.getQualifiedName().toURI());
		
		// 2nd: index the Neo node.
		index.index(neoNode, node);
		
		// 3rd: retain copy of current associations and attach the Resource with this store.
		final Set<Association> copy = node.getAssociations();
		final NeoAssociationKeeper keeper = new NeoAssociationKeeper(node, neoNode, assocHandler);
		AssocKeeperAccess.setAssociationKeeper(node, keeper);
		
		// 4th: store all associations.
		for (Association assoc : copy) {
			if (!assoc.isInferred()) {
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
	
}
