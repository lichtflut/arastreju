/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.inferencing.implicit;

import java.util.Set;

import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.views.SNClass;

/**
 * <p>
 *  Inferencer for rdf:type.<br />
 *  If <code>A rdf:type B</code> and <code>B rdfs:subClassOf C</code> <br />
 *  --> <code>A rdf:type C</code> 
 * </p>
 * 
 * <p>
 * 	Created Sep 2, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class TypeInferencer implements Inferencer {
	
	/**
	 * {@inheritDoc}
	 */
	public void addInferenced(Statement stmt, Set<Statement> target) {
		if (!RDF.TYPE.equals(stmt.getPredicate())) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE, 
					"Expected rdf:type but was " + stmt.getPredicate());
		}
		final SNClass clazz = stmt.getObject().asResource().asClass();
		final Set<SNClass> allClasses = clazz.getSuperClasses();
		for (SNClass current : allClasses) {
			target.add(new DetachedStatement(stmt.getSubject(), RDF.TYPE, current, stmt.getContexts()));
		}
	}

}
