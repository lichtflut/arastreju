package org.arastreju.bindings.memory.storage;

import org.arastreju.bindings.memory.conversation.MemConversationContext;
import org.arastreju.bindings.memory.keepers.MemAssocKeeper;
import org.arastreju.bindings.memory.tx.MemTransactionProvider;
import org.arastreju.sge.ConversationContext;
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
public class MemConnection extends AbstractGraphDataConnection<MemAssocKeeper> {

    public MemConnection(MemStorage storage) {
        super(storage, new MemTransactionProvider());
    }

}
