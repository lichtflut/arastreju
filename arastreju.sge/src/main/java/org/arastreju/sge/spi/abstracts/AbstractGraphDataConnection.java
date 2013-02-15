package org.arastreju.sge.spi.abstracts;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.GraphDataStore;
import org.arastreju.sge.spi.PhysicalNodeID;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Abstract base of graph data connections.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractGraphDataConnection implements GraphDataConnection {

    private final Set<WorkingContext> openConversations = new HashSet<WorkingContext>();

    private final GraphDataStore store;

    private final IndexProvider indexProvider;

    // ----------------------------------------------------

    protected AbstractGraphDataConnection(GraphDataStore store, IndexProvider indexProvider) {
        this.store = store;
        this.indexProvider = indexProvider;
    }

    // ----------------------------------------------------

    @Override
    public AttachedAssociationKeeper find(QualifiedName qn) {
        return store.find(qn);
    }

    @Override
    public AttachedAssociationKeeper create(QualifiedName qn) {
        return store.create(qn);
    }

    @Override
    public void remove(QualifiedName qn) {
        store.remove(qn);
    }

    @Override
    public boolean addAssociation(PhysicalNodeID id, Statement assoc) {
        return store.addAssociation(id, assoc);
    }

    @Override
    public boolean removeAssociation(PhysicalNodeID id, Statement assoc) {
        throw new NotYetImplementedException();
    }

    @Override
    public void resolveAssociations(AttachedAssociationKeeper associationKeeper) {
        throw new NotYetImplementedException();
    }

    // ----------------------------------------------------

    @Override
    public void register(WorkingContext conversationContext) {
        openConversations.add(conversationContext);
    }

    @Override
    public void unregister(WorkingContext conversationContext) {
        openConversations.remove(conversationContext);
    }

    /**
     * Called when a resource has been modified by conversation context belonging to this graph data connection.
     * @param qn The qualified name of the modified resource.
     * @param context The context, where the modification occurred.
     */
    @Override
    public void notifyModification(QualifiedName qn, WorkingContext context) {
        List<WorkingContext> copy = new ArrayList<WorkingContext>(openConversations);
        for (WorkingContext conversation : copy) {
            if (!conversation.equals(context)) {
                conversation.onModification(qn, context);
            }
        }
    }

    /**
     * Close the connection and free all resources.
     */
    @Override
    public void close() {
        List<WorkingContext> copy = new ArrayList<WorkingContext>(openConversations);
        // iterating over copy because original will be remove itself while closing.
        for (WorkingContext cc : copy) {
            cc.close();
        }
    }

    // ----------------------------------------------------

    @Override
    public GraphDataStore getStore() {
        return store;
    }

    @Override
    public IndexProvider getIndexProvider() {
        return indexProvider;
    }
}
