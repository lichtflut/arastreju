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

import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.persistence.ResourceResolver;

import java.util.Set;

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
	
	private final ResourceResolver resolver;
	
	// ----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param resolver The resource resolver.
	 */
	public TypeInferencer(final ResourceResolver resolver) {
		this.resolver = resolver;
	}
	
	// ----------------------------------------------------
	
	public void addInferenced(Statement stmt, Set<Statement> target) {
		if (RDF.TYPE.equals(stmt.getPredicate()) && stmt.getObject().isResourceNode()) {
			final ResourceNode resolved = resolver.resolve(stmt.getObject().asResource());
			final SNClass clazz = SNClass.from(resolved);
			final Set<SNClass> allClasses = clazz.getSuperClasses();
			for (SNClass current : allClasses) {
				target.add(new DetachedStatement(stmt.getSubject(), RDF.TYPE, current, stmt.getContexts()));
			}
		}
	}

}
