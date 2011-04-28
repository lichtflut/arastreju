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

import java.util.ArrayList;
import java.util.List;

import org.arastreju.bindings.neo4j.impl.NeoDataStore;
import org.arastreju.bindings.neo4j.impl.TxAction;
import org.arastreju.bindings.neo4j.mapping.RelationMapper;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.query.QueryManager;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.index.IndexHits;
import org.neo4j.index.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Neo specific implementation of {@link QueryManager}.
 * </p>
 *
 * <p>
 * 	Created Jan 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoQueryManager extends QueryManager implements NeoConstants {

	private final NeoDataStore store;
	
	private final Logger logger = LoggerFactory.getLogger(NeoQueryManager.class);

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 */
	public NeoQueryManager(final NeoDataStore store) {
		this.store = store;
		
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.query.QueryManager#findIncomingAssociations(org.arastreju.sge.model.ResourceID)
	 */
	@Override
	public List<Association> findIncomingAssociations(final ResourceID resource) {
		final List<Association> result = new ArrayList<Association>();
		final RelationMapper mapper = new RelationMapper(store);

		final Node node = store.getIndexService().getSingleNode(INDEX_KEY_RESOURCE_URI, resource.getQualifiedName().toURI());
		for (Relationship rel : node.getRelationships(Direction.INCOMING)) {
			result.add(mapper.toArasAssociation(rel));
		}
		
		return result;
	}

	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.query.QueryManager#findByTag()
	 */
	@Override
	public List<ResourceNode> findByTag(final String tag) {
		final List<ResourceNode> result = new ArrayList<ResourceNode>();
		store.doTransacted(new TxAction() {
			public void execute(final NeoDataStore store) {
				final IndexService index = store.getIndexService();
				final IndexHits<Node> nodes = index.getNodes(INDEX_KEY_RESOURCE_VALUE, tag);
				for (Node node : nodes) {
					result.add(store.findResource(node));
				}
			}
		});
		logger.debug("found for tag '" + tag + "': " + result);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.query.QueryManager#findByTag(org.arastreju.sge.model.ResourceID, java.lang.String)
	 */
	@Override
	public List<ResourceNode> findByTag(final ResourceID predicate, final String tag) {
		final String property = predicate.getQualifiedName().toURI();
		final List<ResourceNode> result = new ArrayList<ResourceNode>();
		store.doTransacted(new TxAction() {
			public void execute(final NeoDataStore store) {
				final IndexService index = store.getIndexService();
				final IndexHits<Node> nodes = index.getNodes(property, tag);
				for (Node node : nodes) {
					result.add(store.findResource(node));
				}
			}
		});
		logger.debug("found for predicate '" + property + "'and tag '" + tag + "': " + result);
		return result;
	}

}
