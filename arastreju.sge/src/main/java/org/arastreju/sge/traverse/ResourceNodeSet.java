package org.arastreju.sge.traverse;

import org.arastreju.sge.model.nodes.ResourceNode;

import java.util.Collection;

/**
 * @author Oliver Tigges
 */
public class ResourceNodeSet extends NodeSet<ResourceNode> {

    public ResourceNodeSet() {
    }

    public ResourceNodeSet(ResourceNode... nodes) {
        super(nodes);
    }

    public ResourceNodeSet(Collection<ResourceNode> nodes) {
        super(nodes);
    }

}
