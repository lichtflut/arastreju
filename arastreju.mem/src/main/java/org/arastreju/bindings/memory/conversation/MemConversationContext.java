package org.arastreju.bindings.memory.conversation;

import org.arastreju.bindings.memory.tx.MemTransactionProvider;
import org.arastreju.sge.context.Context;
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
public class MemConversationContext extends AbstractConversationContext {

    public MemConversationContext(Context primary, Context... readContexts) {
       super(primary, readContexts);
    }

    public MemConversationContext() {
    }

    // ----------------------------------------------------

    @Override
    protected void clearCaches() {
    }

    @Override
    public TxProvider getTxProvider() {
        return new MemTransactionProvider();
    }
}
