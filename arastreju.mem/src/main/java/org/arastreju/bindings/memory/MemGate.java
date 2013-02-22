package org.arastreju.bindings.memory;

import org.arastreju.bindings.memory.conversation.MemConversation;
import org.arastreju.bindings.memory.conversation.MemConversationContext;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.abstracts.AbstractArastrejuGate;
import org.arastreju.sge.spi.abstracts.WorkingContext;

/**
 * <p>
 *  Gate for the Arastreju embedded in memory datastore.
 * </p>

 * <p>
 * Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemGate extends AbstractArastrejuGate {

    public MemGate(GraphDataConnection connection, DomainIdentifier domainIdentifier) {
        super(connection, domainIdentifier);
    }

    // ----------------------------------------------------

    @Override
    protected WorkingContext newWorkingContext(GraphDataConnection connection) {
        return new MemConversationContext(connection);
    }

    @Override
    protected Conversation newConversation(WorkingContext ctx) {
        return new MemConversation(ctx);
    }

}
