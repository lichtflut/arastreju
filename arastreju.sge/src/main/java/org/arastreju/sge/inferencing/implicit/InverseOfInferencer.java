/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.inferencing.implicit;

import java.util.Set;

import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.views.SNProperty;
import org.arastreju.sge.persistence.ResourceResolver;

/**
 * <p>
 *  Inferencer for owl:inverseOf.
 * </p>
 *
 * <p>
 * 	Created Nov 21, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class InverseOfInferencer implements Inferencer {
	
	private final ResourceResolver resolver;

	// ----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param resolver The resource resolver.
	 */
	public InverseOfInferencer(final ResourceResolver resolver) {
		this.resolver = resolver;
	}

	// ----------------------------------------------------

	/** 
	 * {@inheritDoc}
	 */
	public void addInferenced(final Statement stmt, final Set<Statement> target) {
		final SNProperty property = resolver.resolve(stmt.getPredicate()).asProperty();
		property.getInverseProperties();
	}

}
