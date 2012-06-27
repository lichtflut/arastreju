package org.arastreju.sge.traverse;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.ValueNode;

import java.util.Collection;

/**
 * @author Oliver Tigges
 */
public class ValueNodeSet extends NodeSet<ValueNode> {

    public ValueNodeSet() {
    }

    public ValueNodeSet(ValueNode... nodes) {
        super(nodes);
    }

    public ValueNodeSet(Collection<ValueNode> nodes) {
        super(nodes);
    }
}
