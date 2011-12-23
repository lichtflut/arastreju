/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import org.arastreju.sge.inferencing.CompoundInferencer;
import org.arastreju.sge.inferencing.implicit.InverseOfInferencer;
import org.arastreju.sge.persistence.ResourceResolver;

/**
 * <p>
 *  Inferencer for Neo 4j.
 * </p>
 *
 * <p>
 * 	Created Dec 1, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoHardInferencer extends CompoundInferencer {

	public NeoHardInferencer(final ResourceResolver resolver) {
		addInferencer(new InverseOfInferencer(resolver));
	}
	
}
