package org.arastreju.bindings.memory.conversation;

import org.arastreju.bindings.memory.keepers.MemAssociationKeeper;
import org.arastreju.bindings.memory.storage.MemConnection;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.abstracts.AbstractConversationContext;
import org.arastreju.sge.spi.abstracts.WorkingContext;

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
public class MemConversationContext extends AbstractConversationContext<MemAssociationKeeper> {

    public MemConversationContext(MemConnection connection) {
        super(connection);
    }

    public MemConversationContext(MemConnection connection, Context primary, Context... readContexts) {
       super(connection, primary, readContexts);
    }

}
