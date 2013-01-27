package org.arastreju.bindings.memory.keepers;

import org.arastreju.bindings.memory.conversation.MemConversationContext;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AbstractAssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;

import java.util.Set;

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
public class MemAssociationKeeper extends AbstractAssociationKeeper {

    private ConversationContext context;
    private QualifiedName qn;

    // ----------------------------------------------------

    public MemAssociationKeeper(QualifiedName qn) {
        this.qn = qn;
    }

    public MemAssociationKeeper(Set<Statement> associations) {
        super(associations);
    }

    public MemAssociationKeeper(MemConversationContext context) {
        this.context = context;
    }

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
    public ConversationContext getConversationContext() {
        return context;
    }

    @Override
    public void setConversationContext(ConversationContext context) {
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
