package org.arastreju.bindings.memory;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.bindings.memory.conversation.MemConversationContext;
import org.arastreju.bindings.memory.conversation.MemModelingConversation;
import org.arastreju.bindings.memory.storage.MemStorage;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.Organizer;
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

    private MemStorage storage;

    // ----------------------------------------------------

    public MemGate(MemStorage storage, DomainIdentifier domainIdentifier) {
        super(domainIdentifier);
        this.storage = storage;
    }

    // ----------------------------------------------------

    @Override
    public Conversation startConversation() {
        MemConversationContext cc = new MemConversationContext(storage);
        initContext(cc);
        return new MemModelingConversation(cc);
    }

    @Override
    public Conversation startConversation(Context primary, Context... readContexts) {
        MemConversationContext cc = new MemConversationContext(storage, primary, readContexts);
        return new MemModelingConversation(cc);
    }

    @Override
    public Organizer getOrganizer() {
        throw new NotYetImplementedException();
    }

    @Override
    public void close() {
        throw new NotYetImplementedException();
    }
}
