/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.mapping;

import org.arastreju.bindings.neo4j.ArasRelTypes;
import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.SNValueNeo;
import org.arastreju.bindings.neo4j.impl.ResourceResolver;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNContext;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Relationship;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

/**
 * <p>
 *  [DESCRIPTION]
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
	
	public Association toArasAssociation(final Relationship rel){
		SemanticNode object = null;
		Context ctx = null;
		if (rel.isType(ArasRelTypes.REFERENCE)){
			object = resolver.findResource(rel.getEndNode());	
		} else if (rel.isType(ArasRelTypes.VALUE)){
			object = new SNValueNeo(rel.getEndNode());
		}
		
		final ResourceNode subject =  resolver.findResource(rel.getStartNode());	
		
		final ResourceNode predicate = resolver.findResource(new QualifiedName(rel.getProperty(PROPERTY_URI).toString()));
		
		if (rel.hasProperty(PROPERTY_CONTEXT_URI)){
			ResourceNode node = resolver.findResource(new QualifiedName(rel.getProperty(PROPERTY_CONTEXT_URI).toString()));
			if (node instanceof Context){
				ctx = (Context) node;
			} else {
				ctx = new SNContext(node);
			}
		}
		
		// just create the association, it will be implicitly added to the subject by the create method.
		Association.create(subject, predicate, object, ctx);
		
		throw new NotYetImplementedException();
	}

}
