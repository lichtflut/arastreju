package org.arastreju.sge.persistence;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Resolver for resources.
 * <p/>
 *
 * <p>
 *  Created Jul 2, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ResourceResolver {

    /**
     * Resolve a resource identifier.
     * @param rid The resource identifier.
     * @return The existing or newly created ResourceNode for this identifier.
     */
    ResourceNode resolve(ResourceID rid);

    /**
     * Find a resource by it's qualified name.
     * @param qn The qualified name.
     * @return The resource node or null.
     */
    ResourceNode findResource(QualifiedName qn);

}
