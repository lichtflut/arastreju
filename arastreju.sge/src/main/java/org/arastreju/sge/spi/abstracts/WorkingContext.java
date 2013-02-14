package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;

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
     * Add a new Association to given keeper and store it.
     * @param associationKeeper The keeper, which shall be the subject in the new association.
     * @param assoc The Association.
     */
    void addAssociation(T associationKeeper, Statement assoc);

    /**
    * Remove the given association.
    * @param associationKeeper The keeper.
    * @param assoc The association.
    * @return true if the association has been removed.
    */
    boolean removeAssociation(T associationKeeper, Statement assoc);

    /**
     * Resolve the associations of given association keeper.
     * @param associationKeeper The association keeper to be resolved.
     */
    void resolveAssociations(T associationKeeper);

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

    // ----------------------------------------------------

    TxProvider getTxProvider();

    IndexProvider getIndexProvider();

    T lookup(QualifiedName qn);

    T find(QualifiedName qn);

    T create(QualifiedName qn);

    void remove(QualifiedName qn);

    void attach(QualifiedName qn, T keeper);

    void detach(QualifiedName qn);
}
