package org.arastreju.sge.spi.uow;

import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.spi.AssocKeeperAccess;
import org.arastreju.sge.spi.AssociationListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Listens for new and deleted statements and calculates new or obsolete inferences.
 *  Only hard inferences are handled here. Soft inferences are only stored in the index.
 * </p>
 *
 * <p>
 *  Created Feb. 14, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class InferencingInterceptor implements AssociationListener {

    private AssociationListener target;

    private List<Inferencer> inferencers = new ArrayList<Inferencer>();

    // ----------------------------------------------------

    public InferencingInterceptor(AssociationListener target) {
        this.target = target;
    }

    // ----------------------------------------------------

    public InferencingInterceptor add(Inferencer... inferencer) {
        Collections.addAll(inferencers, inferencer);
        return this;
    }

    // ----------------------------------------------------

    @Override
    public void onCreate(Statement stmt) {
        if (!stmt.getObject().isResourceNode()) {
            return;
        }
        AttachedAssociationKeeper newSubject = getObjectAsKeeper(stmt);
        for (Statement newStatement : inference(stmt)) {
            target.onCreate(newStatement);
        }
    }

    @Override
    public void onRemove(Statement stmt) {
        if (!stmt.getObject().isResourceNode()) {
            return;
        }
        AttachedAssociationKeeper newSubject = getObjectAsKeeper(stmt);
        for (Statement removedStatement : inference(stmt)) {
            if (newSubject.getAssociations().contains(removedStatement)) {
                target.onRemove(removedStatement);
            }
        }
    }

    // ----------------------------------------------------

    private Set<Statement> inference(Statement stmt) {
        Set<Statement> result = new HashSet<Statement>();
        for (Inferencer inferencer : inferencers) {
            inferencer.addInferenced(stmt, result);
        }
        return result;
    }

    private AttachedAssociationKeeper getObjectAsKeeper(Statement stmt) {
        SemanticNode object = stmt.getObject();
        if (object.isResourceNode()) {
            AssociationKeeper keeper = AssocKeeperAccess.getInstance().getAssociationKeeper(object.asResource());
            return (AttachedAssociationKeeper) keeper;
        } else {
            throw new IllegalStateException("Cannot create inverted statement, if object is a value node: " + stmt);
        }
    }

}
