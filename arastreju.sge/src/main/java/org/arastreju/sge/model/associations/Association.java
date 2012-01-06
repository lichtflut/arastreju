/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.model.associations;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.AbstractStatement;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  A statement attached to it's subject node.
 * </p>
 *
 * <p>
 * 	Created Jan 6, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class Association extends AbstractStatement {

	/**
	 * Creates a new association.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @param object The object.
	 * @param ctx The contexts of this statement.
	 */
	public Association(ResourceNode subject, ResourceID predicate, SemanticNode object, Context... ctx) {
		super(subject, predicate, object, ctx);
		if (!subject.getAssociations().contains(this)) {
			subject.addAssociation(predicate, object, ctx);
		}
	}
	
}
