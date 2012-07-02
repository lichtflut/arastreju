package org.arastreju.sge.persistence;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;

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

    ResourceNode resolve(ResourceID rid);

}
