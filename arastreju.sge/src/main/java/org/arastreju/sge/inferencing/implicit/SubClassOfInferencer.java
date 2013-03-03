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
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.persistence.ResourceResolver;

import java.util.Set;

/**
 * <p>
 *  Inferencer for rdfs:subClassOf.
 *  For s rdfs:subClassOf c will be inferred
 *  <ul>
 *      <li>s rfd:type rdfs:Class</li>
 *      <li>for each c rdfs:subClassOf x --> s rdfs:subClassOf x</li>
 *  </ul>
 * </p>
 *
 * <p>
 *  Created 08.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class SubClassOfInferencer implements Inferencer {

    private final ResourceResolver resolver;

    // ----------------------------------------------------

    /**
     * Constructor.
     * @param resolver The resource resolver.
     */
    public SubClassOfInferencer(final ResourceResolver resolver) {
        this.resolver = resolver;
    }

    // ----------------------------------------------------


    @Override
    public void addInferenced(Statement stmt, Set<Statement> target) {
        if (RDFS.SUB_CLASS_OF.equals(stmt.getPredicate())) {
            target.add(new DetachedStatement(stmt.getSubject(), RDF.TYPE, RDFS.CLASS));
            if (stmt.getObject().isResourceNode()) {
                final ResourceNode resolved = resolver.resolve(stmt.getObject().asResource());
                final SNClass clazz = SNClass.from(resolved);
                for (SNClass current : clazz.getSuperClasses()) {
                    target.add(new DetachedStatement(stmt.getSubject(), RDFS.SUB_CLASS_OF, current, stmt.getContexts()));
                }
            }
        }
    }
}
