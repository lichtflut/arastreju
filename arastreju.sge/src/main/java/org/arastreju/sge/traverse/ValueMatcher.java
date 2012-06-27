package org.arastreju.sge.traverse;

import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;

import java.util.Collection;

/**
 * @author Oliver Tigges
 */
public abstract class ValueMatcher implements Matcher {

    public static ValueMatcher equals(final String s) {
        return new ValueMatcher() {
            @Override
            public boolean matches(ValueNode value) {
                return value.asText().getStringValue().equals(s);
            }
        };
    }


    // ----------------------------------------------------

    @Override
    public final boolean matches(Collection<? extends SemanticNode> nodes) {
        for (SemanticNode node : nodes) {
            if (node.isValueNode() && matches(node.asValue())) {
                return true;
            }
        }
        return false;
    }

    // ----------------------------------------------------

    public abstract boolean matches(ValueNode value);

}
