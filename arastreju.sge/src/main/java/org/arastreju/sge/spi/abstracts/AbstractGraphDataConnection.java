package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.GraphDataStore;

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
public abstract class AbstractGraphDataConnection<T extends AssociationKeeper> implements GraphDataConnection<T> {

    private final Set<WorkingContext<T>> openConversations = new HashSet<WorkingContext<T>>();

    private final GraphDataStore<T> store;

    private final TxProvider txProvider;

    // ----------------------------------------------------

    protected AbstractGraphDataConnection(GraphDataStore<T> store, TxProvider txProvider) {
        this.store = store;
        this.txProvider = txProvider;
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
     * Close the connection and free all resources.
     */
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
    public TxProvider getTxProvider() {
        return txProvider;
    }

    // ----------------------------------------------------

    protected Set<WorkingContext<T>> getOpenConversations() {
        return openConversations;
    }

}
