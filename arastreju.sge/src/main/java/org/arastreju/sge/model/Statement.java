/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.model;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  Representation of a statement consisting of subject, predicate and object.
 *  Each statement exists in one or more contexts.
 * </p>
 *
 * <p>
 * 	Created May 5, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Statement {

	ResourceID getSubject(); 
	
	ResourceID getPredicate();
	
	SemanticNode getObject();

	Context[] getContexts();
	
}
