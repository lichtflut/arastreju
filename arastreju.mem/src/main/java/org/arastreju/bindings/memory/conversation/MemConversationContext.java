package org.arastreju.bindings.memory.conversation;

import org.arastreju.bindings.memory.keepers.MemAssocKeeper;
import org.arastreju.bindings.memory.storage.MemConnection;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.naming.QualifiedName;
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

    public MemConversationContext(MemConnection connection) {
        super(connection);
    }

    public MemConversationContext(MemConnection connection, Context primary, Context... readContexts) {
       super(connection, primary, readContexts);
    }

    // ----------------------------------------------------

    @Override
    public void onModification(QualifiedName qualifiedName, ConversationContext otherContext) {
        // TODO: implement
    }
}
