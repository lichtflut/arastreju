package org.arastreju.sge.spi;

import org.arastreju.sge.model.associations.AttachedAssociationKeeper;

/**
 * <p>
 *  Resolves associations of a resource using the physical store.
 * </p>
 * <p/>
 * <p>
 * Created 22.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface AssociationResolver {

    /**
     * Resolve the associations.
     * @param keeper The owner of the associations.
     */
    void resolveAssociations(AttachedAssociationKeeper keeper);

}
