package org.arastreju.bindings.memory.conversation;

import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.impl.WorkingContextImpl;

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
public class MemConversationContext extends WorkingContextImpl {

    public MemConversationContext(GraphDataConnection connection) {
        super(connection);
    }


}
