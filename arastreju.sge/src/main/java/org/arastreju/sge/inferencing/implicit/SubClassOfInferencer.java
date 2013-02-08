package org.arastreju.sge.inferencing.implicit;

import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
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
        if (!RDFS.SUB_CLASS_OF.equals(stmt.getPredicate())) {
            throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE,
                    "Expected rdfs:subClassOf but was " + stmt.getPredicate());
        }

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
