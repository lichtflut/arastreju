package org.arastreju.bindings.memory.conversation;

import org.arastreju.sge.spi.impl.ConversationImpl;
import org.arastreju.sge.spi.WorkingContext;

/**
 * <p>
 *  The conversation context.
 * </p>

 * <p>
 *  Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemConversation extends ConversationImpl {

    /**
     * Create a new conversation.
     * @param conversationContext The context of this conversation.
     */
	public MemConversation(final WorkingContext conversationContext) {
		super(conversationContext);
	}

}
