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

    private final Set<AbstractConversationContext<T>> openConversations = new HashSet<AbstractConversationContext<T>>();

    private final GraphDataStore<T> store;

    private final TxProvider txProvider;

    // ----------------------------------------------------

    protected AbstractGraphDataConnection(GraphDataStore<T> store, TxProvider txProvider) {
        this.store = store;
        this.txProvider = txProvider;
    }

    // ----------------------------------------------------

    /**
     * Find a resource.
     * @param qn The resource's qualified name.
     * @return The association keeper or null.
     */
    public T find(QualifiedName qn) {
        return store.find(qn);
    }

    /**
     * Create a new resource.
     * @param qn The resource's qualified name.
     * @return The new association keeper.
     */
    public T create(QualifiedName qn) {
        return store.create(qn);
    }

    // ----------------------------------------------------

    public void register(AbstractConversationContext<T> conversationContext) {
        openConversations.add(conversationContext);
    }

    public void unregister(AbstractConversationContext<T> conversationContext) {
        openConversations.remove(conversationContext);
    }

    /**
     * (re-)open the connection.
     */
    public void open() {
    }

    /**
     * Close the connection and free all resources.
     */
    public void close() {
        List<AbstractConversationContext<T>> copy = new ArrayList<AbstractConversationContext<T>>(openConversations);
        // iterating over copy because original will be remove itself while closing.
        for (AbstractConversationContext<T> cc : copy) {
            cc.close();
        }
    }

    // ----------------------------------------------------

    public GraphDataStore<T> getStore() {
        return store;
    }

    public TxProvider getTxProvider() {
        return txProvider;
    }

    // ----------------------------------------------------

    protected Set<AbstractConversationContext<T>> getOpenConversations() {
        return openConversations;
    }
}
