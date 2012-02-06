/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.traverse;

import java.util.Collection;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Feb 6, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class Walker {
	
	ResourceNode fetchResource() {
		return null;
	}
	
	ResourceNode singleResource() {
		return null;
	}

	Collection<SemanticNode> getAll() {
		return null;
	}
	
	Walker walk(ResourceID predicate) {
		return this;
	}

}
