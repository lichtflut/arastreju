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
package org.arastreju.bindings.neo4j.mapping;

import org.arastreju.bindings.neo4j.ArasRelTypes;
import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.SNValueNeo;
import org.arastreju.bindings.neo4j.impl.ResourceResolver;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNContext;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Relationship;

/**
 * <p>
 *  Simple mapper for Neo4j Relations and Arastreju Statments.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class RelationMapper implements NeoConstants {
	
	private final ResourceResolver resolver;
	
	// -----------------------------------------------------
	
	public RelationMapper(final ResourceResolver resolver) {
		this.resolver = resolver;
	}
	
	// -----------------------------------------------------
	
	public Statement toArasStatement(final Relationship rel){
		SemanticNode object = null;
		Context ctx = null;
		if (rel.isType(ArasRelTypes.REFERENCE)){
			object = resolver.findResource(rel.getEndNode());	
		} else if (rel.isType(ArasRelTypes.VALUE)){
			object = new SNValueNeo(rel.getEndNode());
		}
		
		final ResourceNode subject =  resolver.findResource(rel.getStartNode());	
		
		final ResourceNode predicate = resolver.findResource(new QualifiedName(rel.getProperty(PREDICATE_URI).toString()));
		
		if (rel.hasProperty(CONTEXT_URI)){
			ResourceNode node = resolver.findResource(new QualifiedName(rel.getProperty(CONTEXT_URI).toString()));
			if (node instanceof Context){
				ctx = (Context) node;
			} else if (node != null){
				ctx = new SNContext(node);
			} else {
				throw new IllegalStateException("Context node not found: " + rel.getProperty(CONTEXT_URI));
			}
		}
		

		return new DetachedStatement(subject, predicate, object, ctx);
	}

}
