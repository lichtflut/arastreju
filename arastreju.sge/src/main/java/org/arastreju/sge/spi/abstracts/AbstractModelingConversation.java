package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 * Abstract base for modeling conversations.
 * </p>
 *
 * <p>
 *  Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractModelingConversation implements ModelingConversation {

    private ConversationContext conversationContext;

    // ----------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void addStatement(final Statement stmt) {
        assertActive();
        final ResourceNode subject = resolve(stmt.getSubject());
        SNOPS.associate(subject, stmt.getPredicate(), stmt.getObject(), stmt.getContexts());
    }

    /**
    * {@inheritDoc}
    */
    public boolean removeStatement(final Statement stmt) {
        assertActive();
        final ResourceNode subject = resolve(stmt.getSubject());
        return SNOPS.remove(subject, stmt.getPredicate(), stmt.getObject());
    }

    // ----------------------------------------------------

    protected abstract void assertActive();
}
