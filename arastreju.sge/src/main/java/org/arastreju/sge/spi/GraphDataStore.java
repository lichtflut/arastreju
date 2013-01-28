package org.arastreju.sge.spi;

import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Wrapper of the physical data store.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface GraphDataStore<T extends AssociationKeeper> {

    /**
     * Find the resource with given qualified name and wrap it in a new association keeper object.
     * @param qn The qualified name.
     * @return The association keeper or null if not found.
     */
    T find(QualifiedName qn);

    /**
     * Create a new resource with given qualified name.
     * @param qn The qualified name.
     * @return The new association keeper.
     */
    T create(QualifiedName qn);

    /**
     * Remove the resource identified by qualified name from the store.
     * @param qn The qualified name.
     */
    void remove(QualifiedName qn);
}
