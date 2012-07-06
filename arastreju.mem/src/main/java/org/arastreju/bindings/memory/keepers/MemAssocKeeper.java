package org.arastreju.bindings.memory.keepers;

import org.arastreju.sge.model.associations.AbstractAssociationKeeper;

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
public class MemAssocKeeper extends AbstractAssociationKeeper {

    @Override
    protected void resolveAssociations() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isAttached() {
        return true;
    }

}
