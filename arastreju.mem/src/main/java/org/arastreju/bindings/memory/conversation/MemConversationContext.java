package org.arastreju.bindings.memory.conversation;

import org.arastreju.bindings.memory.keepers.MemAssocKeeper;
import org.arastreju.bindings.memory.storage.MemConnection;
import org.arastreju.bindings.memory.tx.MemTransactionProvider;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.abstracts.AbstractConversationContext;

/**
 * <p>
 *  Conversation context specific for memory binding.
 * </p>

 * <p>
 *  Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemConversationContext extends AbstractConversationContext<MemAssocKeeper> {

    private final MemConnection connection;

    // ----------------------------------------------------

    public MemConversationContext(MemConnection connection) {
        this.connection = connection;
    }

    public MemConversationContext(MemConnection connection, Context primary, Context... readContexts) {
       super(primary, readContexts);
       this.connection = connection;
    }

    // ----------------------------------------------------

    /**
     * Find the resource in this conversation context or in underlying data store.
     * @param qn The resource's qualified name.
     * @return The association keeper or null.
     */
    public MemAssocKeeper find(QualifiedName qn) {
        assertActive();
        MemAssocKeeper registered = lookup(qn);
        if (registered != null) {
            return registered;
        }
        MemAssocKeeper existing = connection.find(qn);
        if (existing != null) {
            MemAssocKeeper keeper = connection.find(qn);
            attach(qn, keeper);
            return keeper;
        } else {
            return null;
        }
    }

    /**
     * @param qn The resource's qualified name.
     * @return The association keeper or null;
     */
    public MemAssocKeeper create(QualifiedName qn) {
        assertActive();
        MemAssocKeeper keeper = connection.create(qn);
        attach(qn, keeper);
        return keeper;
    }

    // ----------------------------------------------------


    @Override
    protected void onClose() {
        clearCaches();
    }

    @Override
    public TxProvider getTxProvider() {
        return new MemTransactionProvider();
    }
}
