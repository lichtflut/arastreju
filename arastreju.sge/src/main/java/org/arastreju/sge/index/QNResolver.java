package org.arastreju.sge.index;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Resolves a qualified name.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface QNResolver {

    /**
     * Resolve a resource node by it's qualified name.
     * @param qn The qualified name.
     * @return The node or null if not found.
     */
    ResourceNode resolve(QualifiedName qn);

}
