package org.arastreju.sge.spi.uow;

import org.arastreju.sge.index.IndexUpdator;
import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.spi.AssociationListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Listens for new and deleted statements during a transaction. When the transaction is done
 *  the index will be updated.
 * </p>
 *
 * <p>
 *  Created Feb. 14, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class IndexUpdateUOW implements UnitOfWork, AssociationListener {

    private Set<ResourceID> modified = new HashSet<ResourceID>();

    private List<Inferencer> inferencers = new ArrayList<Inferencer>();

    private final IndexUpdator indexUpdator;

    // ----------------------------------------------------

    public IndexUpdateUOW(IndexUpdator indexUpdator) {
        this.indexUpdator = indexUpdator;
    }

    // ----------------------------------------------------

    public IndexUpdateUOW add(Inferencer... inferencer) {
        Collections.addAll(inferencers, inferencer);
        return this;
    }

    // ----------------------------------------------------

    @Override
    public void onCreate(Statement stmt) {
        //modified.add(stmt.getSubject());
        update(stmt.getSubject());

    }

    @Override
    public void onRemove(Statement stmt) {
        //modified.add(stmt.getSubject());
        update(stmt.getSubject());
    }

    // ----------------------------------------------------

    @Override
    public void finish() {
        for (ResourceID rid : modified) {
            update(rid);
        }
    }

    private void update(ResourceID rid) {
        ResourceNode node = rid.asResource();
        if (!node.isAttached()) {
            throw new IllegalStateException("Didn't expect a non attached node here: " + modified);
        }
        indexUpdator.index(node);
    }
}
