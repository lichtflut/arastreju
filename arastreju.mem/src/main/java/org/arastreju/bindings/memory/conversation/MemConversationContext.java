package org.arastreju.bindings.memory.conversation;

import org.arastreju.bindings.memory.keepers.MemAssocKeeper;
import org.arastreju.bindings.memory.storage.MemStorage;
import org.arastreju.bindings.memory.tx.MemTransactionProvider;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.abstracts.AbstractConversationContext;

import java.util.HashMap;
import java.util.Map;

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
public class MemConversationContext extends AbstractConversationContext {

    private final Map<QualifiedName, MemAssocKeeper> register = new HashMap<QualifiedName, MemAssocKeeper>();

    private final MemStorage storage;

    // ----------------------------------------------------

    public MemConversationContext(MemStorage storage) {
        this.storage = storage;
    }

    public MemConversationContext(MemStorage storage, Context primary, Context... readContexts) {
       super(primary, readContexts);
        this.storage = storage;
    }

    // ----------------------------------------------------

    /**
     * @param qn The resource's qualified name.
     * @return The association keeper or null;
     */
    public MemAssocKeeper getAssociationKeeper(QualifiedName qn) {
        assertActive();
        return register.get(qn);
    }

    /**
     * @param qn The resource's qualified name.
     * @param keeper The keeper to be accessed.
     */
    public void attach(QualifiedName qn, MemAssocKeeper keeper) {
        assertActive();
        register.put(qn, keeper);
        keeper.setConversationContext(this);
    }

    /**
     * @param qn The resource's qualified name.
     */
    public void detach(QualifiedName qn) {
        assertActive();
        final MemAssocKeeper removed = register.remove(qn);
        if (removed != null) {
            removed.detach();
        }
    }

    // ----------------------------------------------------

    @Override
    protected void clearCaches() {
        register.clear();
    }

    @Override
    protected void onClose() {
        register.clear();
    }

    @Override
    public TxProvider getTxProvider() {
        return new MemTransactionProvider();
    }
}
