package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.GraphDataConnection;

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
public abstract class AbstractArastrejuGate implements ArastrejuGate {

    private GraphDataConnection connection;
    private final DomainIdentifier domainIdentifier;

    // ----------------------------------------------------

	protected AbstractArastrejuGate(GraphDataConnection connection, DomainIdentifier domainIdentifier) {
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

    // ----------------------------------------------------

    protected abstract WorkingContext newWorkingContext(GraphDataConnection connection);

    protected abstract Conversation newConversation(WorkingContext ctx);

    // ----------------------------------------------------

    protected DomainIdentifier getDomainIdentifier() {
        return domainIdentifier;
    }

    @Override
    public void close() {
        connection.close();
    }

}
