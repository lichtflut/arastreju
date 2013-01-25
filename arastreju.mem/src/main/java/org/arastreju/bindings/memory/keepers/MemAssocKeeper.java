package org.arastreju.bindings.memory.keepers;

import org.arastreju.bindings.memory.conversation.MemConversationContext;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.model.associations.AbstractAssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;

/**
 * <p>
 *  Extension of AssociationKeeper.
 * </p>
 *
 * <p>
 *  Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemAssocKeeper extends AbstractAssociationKeeper {

    private MemConversationContext context;

    // ----------------------------------------------------

    @Override
    protected void resolveAssociations() {
        // do nothing - always resolved.
    }

    @Override
    public boolean isAttached() {
        return context != null;
    }

    // ----------------------------------------------------

    @Override
    public MemConversationContext getConversationContext() {
        return context;
    }

    public void setConversationContext(MemConversationContext context) {
        this.context = context;
    }

    public void detach() {
        context = null;
    }

    // ----------------------------------------------------

    /**
     * Called when being serialized --> Replace by detached association keeper.
     * @return A Detached Association Keeper.
     */
    private Object writeReplace() {
        return new DetachedAssociationKeeper(getAssociationsDirectly());
    }

}
