package org.arastreju.bindings.memory.nodes;

import org.arastreju.bindings.memory.keepers.MemAssocKeeper;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Extension of standard SNResource for in memory nodes.
 * </p>
 *
 * <p>
 *  Created 25.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class SNMemResource extends SNResource {

    public SNMemResource() {
    }

    public SNMemResource(QualifiedName qn) {
        super(qn);
    }

    public SNMemResource(QualifiedName qn, MemAssocKeeper associationKeeper) {
        super(qn, associationKeeper);
    }

}
