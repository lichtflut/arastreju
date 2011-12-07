/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.inferencing.CompoundInferencer;
import org.arastreju.sge.inferencing.implicit.InverseOfInferencer;
import org.arastreju.sge.inferencing.implicit.TypeInferencer;
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
public class NeoInferencer extends CompoundInferencer {

	public NeoInferencer(final ResourceResolver resolver) {
		addInferencer(new TypeInferencer(resolver), RDF.TYPE);
		addInferencer(new InverseOfInferencer(resolver));
	}
	
}
