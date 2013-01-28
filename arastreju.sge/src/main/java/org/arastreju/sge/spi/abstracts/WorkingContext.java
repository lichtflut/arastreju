package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  The working context of a conversation.
 * </p>
 *
 * <p>
 *  Created 27.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface WorkingContext<T extends AssociationKeeper> extends ConversationContext {

    /**
     * Close this context and clear all registered resources.
     */
    void close();

    /**
     * Called when a resource has been modified by another conversation context with same graph data connection.
     * @param qn The qualified name of the modified resource.
     * @param otherContext The other context, where the modification occurred.
     */
    void onModification(QualifiedName qn, WorkingContext<T> otherContext);
}
