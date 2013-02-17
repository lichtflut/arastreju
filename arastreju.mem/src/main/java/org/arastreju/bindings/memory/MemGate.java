package org.arastreju.bindings.memory;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.bindings.memory.conversation.MemConversation;
import org.arastreju.bindings.memory.conversation.MemConversationContext;
import org.arastreju.bindings.memory.storage.MemConnection;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.abstracts.AbstractArastrejuGate;

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

    private MemConnection connection;

    // ----------------------------------------------------

    public MemGate(MemConnection connection, DomainIdentifier domainIdentifier) {
        super(domainIdentifier);
        this.connection = connection;
    }

    // ----------------------------------------------------

    @Override
    public Conversation startConversation() {
        MemConversationContext cc = new MemConversationContext(connection);
        initContext(cc);
        return new MemConversation(cc);
    }

    @Override
    public Conversation startConversation(Context primary, Context... readContexts) {
        MemConversationContext cc = new MemConversationContext(connection, primary, readContexts);
        return new MemConversation(cc);
    }

    @Override
    public void close() {
        throw new NotYetImplementedException();
    }
}
