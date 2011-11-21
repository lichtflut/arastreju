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
import org.arastreju.bindings.neo4j.impl.ContextAccess;
import org.arastreju.bindings.neo4j.impl.NeoResourceResolver;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Relationship;

/**
 * <p>
 *  Simple mapper for Neo4j Relations and Arastreju Statements.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class RelationMapper implements NeoConstants {
	
	private final NeoResourceResolver resolver;
	
	// -----------------------------------------------------
	
	public RelationMapper(final NeoResourceResolver resolver) {
		this.resolver = resolver;
	}
	
	// -----------------------------------------------------
	
	/**
	 * Converts a Neo4j relationship to an Arastreju Statement.
	 */
	public Statement toArasStatement(final Relationship rel){
		SemanticNode object = null;
		if (rel.isType(ArasRelTypes.REFERENCE)){
			object = resolver.resolveResource(rel.getEndNode());	
		} else if (rel.isType(ArasRelTypes.VALUE)){
			object = new SNValueNeo(rel.getEndNode());
		}
		
		final ResourceNode subject =  resolver.resolveResource(rel.getStartNode());	
		final ResourceNode predicate = resolver.findResource(new QualifiedName(rel.getProperty(PREDICATE_URI).toString()));
		final Context[] ctx = new ContextAccess(resolver).getContextInfo(rel);
		
		return new DetachedStatement(subject, predicate, object, ctx);
	}

}
