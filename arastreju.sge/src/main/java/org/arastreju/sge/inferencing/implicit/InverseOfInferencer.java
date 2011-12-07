/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.inferencing.implicit;

import java.util.Set;

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

	private final Logger logger = LoggerFactory.getLogger(InverseOfInferencer.class);
	
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
		logger.info("Looking for inverse properties of: " + property);
		for(SNProperty sp : property.getSuperProperties()) {
			Set<SemanticNode> invertedPredicates = SNOPS.objects(sp, Aras.INVERSE_OF);
			if (!invertedPredicates.isEmpty()) {
				addInverted(stmt, invertedPredicates, target);
			}
		}
	}

	/**
	 * @param stmt
	 * @param invertedPredicates
	 */
	private void addInverted(Statement stmt, Set<SemanticNode> invertedPredicates, Set<Statement> target) {
		for (SemanticNode sn : invertedPredicates) {
			if (sn.isResourceNode()) {
				final DetachedStatement inverted = new DetachedStatement(stmt.getObject().asResource(), sn.asResource(), 
						stmt.getSubject(), stmt.getContexts()).setInferred(false);
				target.add(inverted);
				logger.info("Added inverted Stmt: " + inverted);
			}
		}
	}

}
