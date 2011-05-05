/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.model;


import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.SemanticNode;


/**
 * <p>
 *  A detached Statement. This means, the statement and it's parts need not to be
 *  attached to any datastore.
 * </p>
 *
 * <p>
 * 	Created May 5, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class DetachedStatement extends AbstractStatement {
	
	/**
	 * Creates a new Statement.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @param object The object.
	 * @param contexts The contexts of this statement.
	 */
	public DetachedStatement(final ResourceID subject, final ResourceID predicate,
			final SemanticNode object, final Context... contexts) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		setContexts(contexts);
	}

}
