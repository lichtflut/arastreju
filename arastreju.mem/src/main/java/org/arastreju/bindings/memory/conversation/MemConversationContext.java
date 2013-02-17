package org.arastreju.bindings.memory.conversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.bindings.memory.storage.MemConnection;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.persistence.ResourceResolver;
import org.arastreju.sge.spi.abstracts.AbstractConversationContext;
import org.arastreju.sge.spi.abstracts.AssociationManager;
import org.arastreju.sge.spi.uow.ResourceResolverImpl;

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
public class MemConversationContext extends AbstractConversationContext {

    private AssociationManager manager;

    // ----------------------------------------------------

    public MemConversationContext(MemConnection connection) {
        super(connection);
        init();
    }

    public MemConversationContext(MemConnection connection, Context primary, Context... readContexts) {
        super(connection, primary, readContexts);
        init();
    }

    // ----------------------------------------------------

    @Override
    public void resolveAssociations(AttachedAssociationKeeper associationKeeper) {
        throw new NotYetImplementedException();
    }

    // ----------------------------------------------------

    @Override
    protected AssociationManager getAssociationManager() {
        return manager;
    }

    // ----------------------------------------------------

    private void init() {
        ResourceResolver resolver = new ResourceResolverImpl(this);
        manager = new AssociationManager(resolver);
    }

}
