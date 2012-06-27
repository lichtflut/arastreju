package org.arastreju.sge.traverse;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;

import java.util.Collection;

/**
 * @author Oliver Tigges
 */
public abstract class ResourceMatcher implements Matcher {

    public static ResourceMatcher equals(final ResourceID rid) {
        return new ResourceMatcher() {
            @Override
            public boolean matches(ResourceNode node) {
                return node.equals(rid);
            }
        };
    }

    // ----------------------------------------------------

    @Override
    public final boolean matches(Collection<? extends SemanticNode> nodes) {
        for (SemanticNode node : nodes) {
            if (node.isResourceNode() && matches(node.asResource())) {
                return true;
            }
        }
        return false;
    }

    public abstract boolean matches(ResourceNode resource);

}
