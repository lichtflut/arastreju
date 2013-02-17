package org.arastreju.sge.spi;

import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.abstracts.WorkingContext;

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
public interface GraphDataConnection {

    /**
     * Find a resource.
     * @param qn The resource's qualified name.
     * @return The association keeper or null.
     */
    AttachedAssociationKeeper find(QualifiedName qn);

    /**
     * Create a new resource.
     * @param qn The resource's qualified name.
     * @return The new association keeper.
     */
    AttachedAssociationKeeper create(QualifiedName qn);

    /**
     * Remove a resource.
     * @param qn The resource's qualified name.
     */
    void remove(QualifiedName qn);

    // ----------------------------------------------------

    GraphDataStore getStore();

    IndexProvider getIndexProvider();

    /**
     * Get the provider for transactions.
     * @return The transaction provider.
     */
    TxProvider getTxProvider();

    // ----------------------------------------------------

    /**
     * Notify the connection about a change for the resource with given qualified name.
     * @param qn The qualified name.
     * @param context The context in which occurred the change.
     */
    void notifyModification(QualifiedName qn, WorkingContext context);

    void register(WorkingContext conversationContext);

    void unregister(WorkingContext conversationContext);

    void close();
}
