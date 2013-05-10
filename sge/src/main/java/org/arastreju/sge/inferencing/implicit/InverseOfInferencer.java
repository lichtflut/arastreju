/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.inferencing.implicit;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNProperty;
import org.arastreju.sge.persistence.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(InverseOfInferencer.class);
	
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

	@Override
	public void addInferenced(final Statement stmt, final Set<Statement> target) {
		final SNProperty property = SNProperty.from(resolver.resolve(stmt.getPredicate()));
		Set<SemanticNode> invertedPredicates = SNOPS.objects(property, Aras.INVERSE_OF);
		if (!invertedPredicates.isEmpty()) {
			addInverted(stmt, invertedPredicates, target);
			LOGGER.debug("Found inverted properties of {} : {}", property, invertedPredicates);
		}
	}
	
	// ----------------------------------------------------

	private void addInverted(Statement originalStmt, Set<SemanticNode> invertedPredicates, Set<Statement> target) {
		for (SemanticNode predicate : invertedPredicates) {
			if (predicate.isResourceNode()) {
				final Statement inverted = new DetachedStatement(originalStmt.getObject().asResource(), predicate.asResource(),
						originalStmt.getSubject(), originalStmt.getContexts());
				target.add(inverted);
				LOGGER.debug("Added inverted Stmt: {} ", inverted);
			}
		}
	}

}
