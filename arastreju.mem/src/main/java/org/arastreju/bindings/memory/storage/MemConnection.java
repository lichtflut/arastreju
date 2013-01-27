package org.arastreju.bindings.memory.storage;

import org.arastreju.bindings.memory.conversation.MemConversationContext;
import org.arastreju.bindings.memory.keepers.MemAssocKeeper;
import org.arastreju.bindings.memory.tx.MemTransactionProvider;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.GraphDataStore;
import org.arastreju.sge.spi.abstracts.AbstractGraphDataConnection;

/**
 * <p>
 *  In-memory specific connection.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemConnection extends AbstractGraphDataConnection<MemConversationContext> {

    private final MemStorage storage;
    private final TxProvider txProvider;

    // ----------------------------------------------------

    public MemConnection(MemStorage storage) {
        this.storage = storage;
        this.txProvider = new MemTransactionProvider();
    }

    // ----------------------------------------------------

    /**
     * Find a resource.
     * @param qn The resource's qualified name.
     * @return The association keeper or null.
     */
    public MemAssocKeeper find(QualifiedName qn) {
        return storage.find(qn);
    }

    /**
     * Create a new resource.
     * @param qn The resource's qualified name.
     * @return The new association keeper.
     */
    public MemAssocKeeper create(QualifiedName qn) {
        return storage.create(qn);
    }

    // ----------------------------------------------------

    @Override
    public GraphDataStore getStore() {
        return storage;
    }

    @Override
    public TxProvider getTxProvider() {
        return txProvider;
    }

}
