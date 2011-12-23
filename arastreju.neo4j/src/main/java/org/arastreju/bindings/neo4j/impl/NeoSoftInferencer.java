/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.inferencing.CompoundInferencer;
import org.arastreju.sge.inferencing.implicit.TypeInferencer;
import org.arastreju.sge.persistence.ResourceResolver;

/**
 * <p>
 *  Inferencer for Neo 4j for soft inferences, i.e. inferences that will only be put in the index,
 *  but not to the database.
 * </p>
 *
 * <p>
 * 	Created Dec 1, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoSoftInferencer extends CompoundInferencer {

	public NeoSoftInferencer(final ResourceResolver resolver) {
		addInferencer(new TypeInferencer(resolver), RDF.TYPE);
	}
	
}
