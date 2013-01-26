package org.arastreju.bindings.memory.storage;

import org.arastreju.bindings.memory.tx.MemTransactionProvider;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.GraphDataStore;

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
public class MemConnection implements GraphDataConnection {

    private final MemStorage storage;
    private final TxProvider txProvider;

    // ----------------------------------------------------

    public MemConnection(MemStorage storage) {
        this.storage = storage;
        txProvider = new MemTransactionProvider();
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
