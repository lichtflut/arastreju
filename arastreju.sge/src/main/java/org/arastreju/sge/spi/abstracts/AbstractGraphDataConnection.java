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
public abstract class AbstractGraphDataConnection<T extends AttachedAssociationKeeper> implements GraphDataConnection<T> {

    private final Set<WorkingContext<T>> openConversations = new HashSet<WorkingContext<T>>();

    private final GraphDataStore<T> store;

    private final IndexProvider indexProvider;

    // ----------------------------------------------------

    protected AbstractGraphDataConnection(GraphDataStore<T> store, IndexProvider indexProvider) {
        this.store = store;
        this.indexProvider = indexProvider;
    }

    // ----------------------------------------------------

    @Override
    public T find(QualifiedName qn) {
        return store.find(qn);
    }

    @Override
    public T create(QualifiedName qn) {
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
    public void resolveAssociations(T associationKeeper) {
        throw new NotYetImplementedException();
    }

    // ----------------------------------------------------

    @Override
    public void register(AbstractConversationContext<T> conversationContext) {
        openConversations.add(conversationContext);
    }

    @Override
    public void unregister(AbstractConversationContext<T> conversationContext) {
        openConversations.remove(conversationContext);
    }

    /**
     * Called when a resource has been modified by conversation context belonging to this graph data connection.
     * @param qn The qualified name of the modified resource.
     * @param context The context, where the modification occurred.
     */
    @Override
    public void notifyModification(QualifiedName qn, WorkingContext<T> context) {
        List<WorkingContext<T>> copy = new ArrayList<WorkingContext<T>>(openConversations);
        for (WorkingContext<T> conversation : copy) {
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
        List<WorkingContext<T>> copy = new ArrayList<WorkingContext<T>>(openConversations);
        // iterating over copy because original will be remove itself while closing.
        for (WorkingContext<T> cc : copy) {
            cc.close();
        }
    }

    // ----------------------------------------------------

    @Override
    public GraphDataStore<T> getStore() {
        return store;
    }

    @Override
    public IndexProvider getIndexProvider() {
        return indexProvider;
    }
}
