package org.arastreju.bindings.memory.storage;

import org.arastreju.bindings.memory.tx.MemTransactionProvider;
import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.persistence.TxProvider;
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
public class MemConnection extends AbstractGraphDataConnection {

    private final MemTransactionProvider txProvider;

    // ----------------------------------------------------

    public MemConnection(MemStorage storage, IndexProvider indexProvider) {
        super(storage, indexProvider);
        txProvider = new MemTransactionProvider();
    }

    // ----------------------------------------------------

    @Override
    public TxProvider getTxProvider() {
        return txProvider;
    }

}
