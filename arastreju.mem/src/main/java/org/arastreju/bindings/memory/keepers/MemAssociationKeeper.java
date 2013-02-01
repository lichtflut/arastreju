package org.arastreju.bindings.memory.keepers;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.abstracts.WorkingContext;

import java.util.Set;

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
public class MemAssociationKeeper extends AttachedAssociationKeeper {

    public MemAssociationKeeper(QualifiedName qn) {
        super(qn);
    }

    public MemAssociationKeeper(QualifiedName qn, Set<Statement> associations) {
        super(qn, associations);
    }

    // ----------------------------------------------------

    @Override
    protected void resolveAssociations() {
        // do nothing - always resolved.
    }

    // ----------------------------------------------------

    /**
     * Called when being serialized --> Replace by detached association keeper.
     * @return A Detached Association Keeper.
     */
    private Object writeReplace() {
        return new DetachedAssociationKeeper(getAssociationsDirectly());
    }

}
