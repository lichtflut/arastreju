package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.index.IndexSearcher;
import org.arastreju.sge.index.IndexUpdator;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
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
public interface WorkingContext extends ConversationContext {

    /**
     * Lookup the qualified name in the register.
     * @param qn The qualified name.
     * @return The association keeper or null.
     */
    AttachedAssociationKeeper lookup(QualifiedName qn);

    /**
     * Find the resource in this conversation context or in underlying data store.
     * @param qn The resource's qualified name.
     * @return The association keeper or null.
     */
    AttachedAssociationKeeper find(QualifiedName qn);

    /**
     * Create a new resource.
     * @param qn The resource's qualified name.
     * @return The attached association keeper or null;
     */
    AttachedAssociationKeeper create(QualifiedName qn);

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
    void attach(QualifiedName qn, AttachedAssociationKeeper keeper);

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
    void addAssociation(AttachedAssociationKeeper associationKeeper, Statement assoc);

    /**
    * Remove the given association.
    * @param associationKeeper The keeper.
    * @param assoc The association.
    * @return true if the association has been removed.
    */
    boolean removeAssociation(AttachedAssociationKeeper associationKeeper, Statement assoc);

    /**
     * Resolve the associations of given association keeper.
     * @param associationKeeper The association keeper to be resolved.
     */
    void resolveAssociations(AttachedAssociationKeeper associationKeeper);

    // ----------------------------------------------------

    /**
     * Get a searcher for indexed elements.
     * @return The index searcher.
     */
    IndexSearcher getIndexSearcher();

    /**
     * Get an updator for indexing of elements.
     * @return The updator.
     */
    IndexUpdator getIndexUpdator();

    // ----------------------------------------------------

    TxProvider getTxProvider();

    /**
     * Close this context and clear all registered resources.
     */
    void close();

    /**
     * Called when a resource has been modified by another conversation context with same graph data connection.
     * @param qn The qualified name of the modified resource.
     * @param otherContext The other context, where the modification occurred.
     */
    void onModification(QualifiedName qn, WorkingContext otherContext);

}
