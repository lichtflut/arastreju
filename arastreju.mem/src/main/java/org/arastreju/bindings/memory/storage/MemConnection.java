package org.arastreju.bindings.memory.storage;

import org.arastreju.bindings.memory.keepers.MemAssociationKeeper;
import org.arastreju.bindings.memory.tx.MemTransactionProvider;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.spi.PhysicalNodeID;
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
public class MemConnection extends AbstractGraphDataConnection<MemAssociationKeeper> {

    public MemConnection(MemStorage storage) {
        super(storage, new MemTransactionProvider());
    }

}
