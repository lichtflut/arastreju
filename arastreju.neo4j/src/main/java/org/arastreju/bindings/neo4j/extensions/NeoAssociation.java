/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.extensions;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  Neo extension of association.
 * </p>
 *
 * <p>
 * 	Created Dec 1, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoAssociation extends Association {

	/**
	 * @param subject
	 * @param predicate
	 * @param object
	 * @param contexts
	 */
	public NeoAssociation(final ResourceNode subject, final ResourceID predicate, final SemanticNode object, final Context... contexts) {
		super(subject, predicate, object, contexts);
	}
	
	// ----------------------------------------------------
	
	@Override
	public NeoAssociation setInferred(boolean inferred) {
		return (NeoAssociation) super.setInferred(inferred);
	}

}
