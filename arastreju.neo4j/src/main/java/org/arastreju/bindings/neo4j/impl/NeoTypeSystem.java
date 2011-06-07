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
package org.arastreju.bindings.neo4j.impl;

import java.util.HashSet;
import java.util.Set;

import org.arastreju.bindings.neo4j.ArasRelTypes;
import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.sge.TypeSystem;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.helpers.Predicate;
import org.neo4j.kernel.Traversal;

/**
 * <p>
 *  Neo specific implementation of TypeSystem.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoTypeSystem implements TypeSystem, NeoConstants {
	
	private final String RDFS_CLASS_URI = RDFS.CLASS.getQualifiedName().toURI();
	private final String RDF_TYPE_URI = RDF.TYPE.getQualifiedName().toString();
	private final String RDFS_SUB_CLASS = RDFS.SUB_CLASS_OF.getQualifiedName().toString();
	
	private final SemanticNetworkAccess store;

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param store The {@link SemanticNetworkAccess}.
	 */
	public NeoTypeSystem(final SemanticNetworkAccess store) {
		this.store = store;
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.TypeSystem#getAllClasses()
	 */
	public Set<SNClass> getAllClasses() {
		final Set<SNClass> result = new HashSet<SNClass>();
		final Node rdfClassNode = store.getIndexService().getSingleNode(INDEX_KEY_RESOURCE_URI, RDFS_CLASS_URI);
		if (rdfClassNode == null){
			throw new IllegalStateException("Node for rdfs:Class does not exist.");
		}

		final TraversalDescription description = 
			Traversal.description()
				.breadthFirst()
				.relationships(ArasRelTypes.REFERENCE, Direction.INCOMING)
				.filter(new Predicate<Path>() {
			public boolean accept(final Path path) {
				final Relationship rel = path.lastRelationship();
				if (rel == null || !rel.hasProperty(PREDICATE_URI)){
					return false;
				}
				final String pred = rel.getProperty(PREDICATE_URI).toString();
				if (RDF_TYPE_URI.equals(pred) && rel.getEndNode().equals(rdfClassNode)){
					return true;	
				} else if (RDFS_SUB_CLASS.equals(pred)) {
					return true;
				} else {
					return false;
				}
			}
		});
		
		final Traverser traverser = description.traverse(rdfClassNode);
		for(Path path : traverser){
			System.out.println("Path: " + path);
			result.add(store.findResource(path.endNode()).asClass());
		}
		
		return result;
	}

}
