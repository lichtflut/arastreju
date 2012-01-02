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
package org.arastreju.bindings.neo4j.query;

import static org.arastreju.sge.SNOPS.uri;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.arastreju.bindings.neo4j.impl.NeoResourceResolver;
import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.bindings.neo4j.mapping.RelationMapper;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.query.QueryManager;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
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
public class NeoQueryManager implements QueryManager {

	private final Logger logger = LoggerFactory.getLogger(NeoQueryManager.class);
	
	private final ResourceIndex index;
	
	private final NeoResourceResolver resolver;

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 */
	public NeoQueryManager(final NeoResourceResolver resolver, final ResourceIndex index) {
		this.resolver = resolver;
		this.index = index;
	}
	
	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public NeoQueryBuilder buildQuery() {
		return new NeoQueryBuilder(index);
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public Set<Statement> findIncomingStatements(final ResourceID resource) {
		final Set<Statement> result = new HashSet<Statement>();
		final RelationMapper mapper = new RelationMapper(resolver);
		final Node node = index.findNeoNode(resource.getQualifiedName());
		if (node != null){
			for (Relationship rel : node.getRelationships(Direction.INCOMING)) {
				result.add(mapper.toArasStatement(rel));
			}
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<ResourceNode> findByType(final ResourceID type) {
		final String typeURI = uri(type);
		final List<ResourceNode> result = index.lookup(RDF.TYPE, typeURI).toList();
		logger.debug("found with rdf:type '" + typeURI + "': " + result);
		return result;
	}

}
