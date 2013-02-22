package org.arastreju.sge.spi.impl;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.WorkingContext;

/**
 * <p>
 *  Abstract base class for an ArasterjuGate.
 * </p>
 *
 * <p>
 *  Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class ArastrejuGateImpl implements ArastrejuGate {

    private final GraphDataConnection connection;

    private final DomainIdentifier domainIdentifier;

    // ----------------------------------------------------

    /**
     * Create and open a new gate.
     * @param connection The connection to the graph data store.
     * @param domainIdentifier The gate context.
     */
	public ArastrejuGateImpl(GraphDataConnection connection, DomainIdentifier domainIdentifier) {
        this.connection = connection;
        this.domainIdentifier = domainIdentifier;
	}

    // ----------------------------------------------------

    @Override
    public Conversation startConversation() {
        return startConversation(domainIdentifier.getInitialContext(), domainIdentifier.getInitialContext());
    }

    @Override
    public Conversation startConversation(Context primary, Context... readContexts) {
        WorkingContext ctx = newWorkingContext(connection);
        ctx.setPrimaryContext(primary);
        ctx.setReadContexts(readContexts);
        return newConversation(ctx);
    }

    @Override
    public void close() {
        connection.close();
    }

    // ----------------------------------------------------

    protected Conversation newConversation(WorkingContext ctx) {
        return new ConversationImpl(ctx);
    }

    protected WorkingContext newWorkingContext(GraphDataConnection connection) {
        return new WorkingContextImpl(connection);
    }

}
