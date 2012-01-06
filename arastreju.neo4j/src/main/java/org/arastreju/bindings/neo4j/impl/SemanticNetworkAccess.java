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
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxAction;
import org.arastreju.sge.persistence.TxResultAction;
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
		final AssociationKeeper keeper = findAssociationKeeper(qn);
		if (keeper != null) {
			return createArasNode(keeper, qn);
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
		AssociationKeeper keeper = index.findAssociationKeeper(qn);
		if (keeper == null){
			keeper = new NeoAssociationKeeper(SNOPS.id(qn), neoNode, assocHandler);
		}
		return createArasNode(keeper, qn);
	}

	// -----------------------------------------------------
	
	/**
	 * Attach the given node if it is not already attached.
	 * @param resource The node to attach.
	 * @return A node attached by guaranty.
	 */
	public void attach(final ResourceNode resource) {
		// 1st: check if node is already attached.
		if (resource.isAttached()){
			return;
		}
		txProvider.doTransacted(new TxAction() {
			public void execute() {
				// 2nd: check if node for qualified name exists and has to be merged
				final AssociationKeeper attachedKeeper = findAssociationKeeper(resource.getQualifiedName());
				if (attachedKeeper != null){
					merge(attachedKeeper, resource);
				} else {
					// 3rd: if resource is really new, create a new Neo node.
					persist(resource);
				}
			}
		});
	}
	
	/**
	 * Unregister the node from the registry and detach the {@link AssociationKeeper}
	 * @param node The node to detach.
	 */
	public void detach(final ResourceNode node){
		index.uncache(node.getQualifiedName());
		AssocKeeperAccess.setAssociationKeeper(node, new DetachedAssociationKeeper(node.getAssociations()));
	}
	
	/**
	 * Reset the given node if it is detached.
	 * @param node The node to be reseted.
	 */
	public void reset(final ResourceNode node) {
		// 1st: check if node is detached.
		if (!node.isAttached()){
			return;
		}
		final AssociationKeeper keeper = findAssociationKeeper(node.getQualifiedName());
		if (keeper != null) {
			AssocKeeperAccess.setAssociationKeeper(node, keeper);
		} else {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE, 
					"Can't find node/keeper for attached node " + node.getQualifiedName());
		}
	}
	
	/**
	 * Remove the node.
	 * @param id The ID.
	 */
	public void remove(final ResourceID id) {
		final ResourceNode node = resolve(id);
		AssocKeeperAccess.getAssociationKeeper(node).getAssociations().clear();
		txProvider.doTransacted(new TxAction() {
			public void execute() {
				new NodeRemover(index).remove(node, false);
			}
		});
		detach(node);
	}
	
	// -----------------------------------------------------

	/**
	 * Close the graph database;
	 */
	public void close() {
		index.clearCache();
	}

	// -----------------------------------------------------
	
	/**
	 * Find the keeper for a qualified name.
	 * @param qn The qualified name.
	 * @return The keeper or null.
	 */
	protected AssociationKeeper findAssociationKeeper(final QualifiedName qn) {
		final AssociationKeeper registered = index.findAssociationKeeper(qn);
		if (registered != null) {
			return registered;
		}
		final Node neoNode = index.findNeoNode(qn);
		if (neoNode != null) {
			return new NeoAssociationKeeper(SNOPS.id(qn), neoNode, assocHandler);
		} else {
			return null;
		}
	}
	
	/**
	 * Create a new Arastreju node for Neo node.
	 * @param neoNode The Neo node.
	 * @param qn The qualified name.
	 * @return The Arastreju node.
	 */
	protected SNResource createArasNode(final Node neoNode, final QualifiedName qn) {
		final NeoAssociationKeeper keeper = new NeoAssociationKeeper(SNOPS.id(qn), neoNode, assocHandler);
		return createArasNode(keeper, qn);
	}
	
	/**
	 * Create a new Arastreju node 'around' given association keeper.
	 * @param keeper The association keeper.
	 * @param qn The qualified name.
	 * @return The Arastreju node.
	 */
	protected SNResourceNeo createArasNode(final AssociationKeeper keeper, final QualifiedName qn) {
		final SNResourceNeo arasNode = new SNResourceNeo(qn, keeper);
		index.cache(arasNode);
		return arasNode;
	}
	
	/**
	 * Create the given resource node in Neo4j DB.
	 * @param node A not yet persisted node.
	 * @return The persisted ResourceNode.
	 */
	protected ResourceNode persist(final ResourceNode node) {
		// 1st: create a corresponding Neo node.
		final Node neoNode = gdbService.createNode();
		neoNode.setProperty(PROPERTY_URI, node.getQualifiedName().toURI());
		
		// 2nd: retain copy of current associations and attach the Resource with this store.
		final Set<Statement> copy = node.getAssociations();
		final NeoAssociationKeeper keeper = new NeoAssociationKeeper(node, neoNode, assocHandler);
		AssocKeeperAccess.setAssociationKeeper(node, keeper);
		
		// 3rd: index the Neo node.
		index.index(neoNode, node);
		
		// 4th: store all associations.
		for (Statement assoc : copy) {
			keeper.addAssociation(assoc);
		}
		
		return node;
	}
	
	/**
	 * Merges all associations from the 'changed' node to the 'attached' keeper and put's keeper in 'changed'.
	 * @param attached The currently attached keeper for this resource.
	 * @param changed An unattached node referencing the same resource.
	 * @return The merged {@link ResourceNode}.
	 */
	protected void merge(final AssociationKeeper attached, final ResourceNode changed) {
		final AssociationKeeper detached = AssocKeeperAccess.getAssociationKeeper(changed);
		for (Statement toBeRemoved : detached.getAssociationsForRemoval()) {
			attached.removeAssociation(toBeRemoved);
		}
		final Set<Statement> currentAssocs = attached.getAssociations();
		for(Statement assoc : detached.getAssociations()){
			if (!currentAssocs.contains(assoc)){
				attached.addAssociation(assoc);
			}
		}
		AssocKeeperAccess.setAssociationKeeper(changed, attached);
	}
	
}
