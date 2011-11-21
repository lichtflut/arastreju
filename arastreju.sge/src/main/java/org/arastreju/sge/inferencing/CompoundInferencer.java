/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.inferencing;

import java.util.Set;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;

import de.lichtflut.infra.data.MultiMap;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Sep 2, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class CompoundInferencer implements Inferencer {
	
	private final MultiMap<ResourceID, Inferencer> predicateMap = new MultiMap<ResourceID, Inferencer>();
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public void addInferenced(final Statement stmt, final Set<Statement> target) {
		final Set<Inferencer> inferencers = predicateMap.getValues(stmt.getPredicate());
		for (Inferencer current : inferencers) {
			current.addInferenced(stmt, target);
		}
	};
	
}
