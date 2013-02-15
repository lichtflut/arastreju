package org.arastreju.sge.spi;

import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Attached resource for usage in data bindings.
 * </p>
 *
 * <p>
 *  Created Feb. 15, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class AttachedResourceNode extends SNResource {

    public AttachedResourceNode(QualifiedName qn, AssociationKeeper associationKeeper) {
        super(qn, associationKeeper);
    }
}
