package org.arastreju.sge.spi;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.abstracts.AbstractConversationContext;

/**
 * <p>
 *  The connection to a graph data store in the scope of a conversation.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface GraphDataConnection<T extends AttachedAssociationKeeper> {

    /**
     * Find a resource.
     * @param qn The resource's qualified name.
     * @return The association keeper or null.
     */
    T find(QualifiedName qn);

    /**
     * Create a new resource.
     * @param qn The resource's qualified name.
     * @return The new association keeper.
     */
    T create(QualifiedName qn);

    /**
     * Remove a resource.
     * @param qn The resource's qualified name.
     */
    void remove(QualifiedName qn);

    // ----------------------------------------------------

    /**
     * Add a new association to a node.
     * @param id The physical id of the node.
     * @param assoc The Association.
     */
    boolean addAssociation(PhysicalNodeID id, Statement assoc);

    /**
     * Remove the given association.
     * @param id The physical id of the node.
     * @param assoc The association.
     * @return true if the association has been removed.
     */
    boolean removeAssociation(PhysicalNodeID id, Statement assoc);

    /**
     * Resolve the associations of given association keeper.
     * @param associationKeeper The association keeper to be resolved.
     */
    void resolveAssociations(T associationKeeper);

    // ----------------------------------------------------

    GraphDataStore<T> getStore();

    TxProvider getTxProvider();

    // ----------------------------------------------------

    void register(AbstractConversationContext<T> conversationContext);

    void unregister(AbstractConversationContext<T> conversationContext);

}
