package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.GraphDataConnection;

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
     * Lookup the qualified name in the register.
     * @param qn The qualified name.
     * @return The association keeper or null.
     */
    T lookup(QualifiedName qn);

    /**
     * Find the resource in this conversation context or in underlying data store.
     * @param qn The resource's qualified name.
     * @return The association keeper or null.
     */
    T find(QualifiedName qn);

    /**
     * Create a new resource.
     * @param qn The resource's qualified name.
     * @return The attached association keeper or null;
     */
    T create(QualifiedName qn);

    /**
     * Remove the resource identified by given qualified name.
     * @param qn The resource's qualified name.
     */
    void remove(QualifiedName qn);

    /**
     * Attach the keeper to this context. The resource will be created if it does not exist yet.
     * @param qn The resource's qualified name.
     * @param keeper The keeper to be accessed.
     */
    void attach(QualifiedName qn, T keeper);

    /**
     * Detach the keeper from the context.
     * @param qn The resource's qualified name.
     */
    void detach(QualifiedName qn);

    // ----------------------------------------------------

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



    // ----------------------------------------------------

    TxProvider getTxProvider();

    IndexProvider getIndexProvider();

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
