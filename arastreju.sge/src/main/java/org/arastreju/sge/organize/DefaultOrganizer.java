package org.arastreju.sge.organize;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.io.StatementContainer;
import org.arastreju.sge.naming.Namespace;

import java.util.Collection;

/**
 * <p>
 *  Default implementation of an organizer.
 * </p>
 * <p/>
 * <p>
 * Created 27.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class DefaultOrganizer extends AbstractOrganizer {

    private final ArastrejuGate gate;

    // ----------------------------------------------------

    public DefaultOrganizer(ArastrejuGate gate) {
        this.gate = gate;
    }

    // ----------------------------------------------------

    @Override
    protected Conversation conversation() {
        return gate.startConversation();
    }

    @Override
    public Collection<Namespace> getNamespaces() {
        return null;
    }

    @Override
    public Collection<Context> getContexts() {
        return null;
    }

    @Override
    public StatementContainer getStatements(Context... ctx) {
        return null;
    }

}
