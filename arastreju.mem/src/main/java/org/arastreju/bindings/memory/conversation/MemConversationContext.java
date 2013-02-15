package org.arastreju.bindings.memory.conversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.bindings.memory.keepers.MemAssociationKeeper;
import org.arastreju.bindings.memory.storage.MemConnection;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.spi.abstracts.AbstractConversationContext;

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

    // ----------------------------------------------------

    @Override
    public void addAssociation(MemAssociationKeeper keeper, Statement assoc) {
        getConnection().getStore().addAssociation(keeper.getPhysicalID(), assoc);
        keeper.addAssociationDirectly(assoc);
    }

    @Override
    public boolean removeAssociation(MemAssociationKeeper keeper, Statement assoc) {
        return getConnection().addAssociation(keeper.getPhysicalID(), assoc);
    }

    @Override
    public void resolveAssociations(MemAssociationKeeper associationKeeper) {
        throw new NotYetImplementedException();
    }

    @Override
    public IndexProvider getIndexProvider() {
        throw new NotYetImplementedException();
    }
}
