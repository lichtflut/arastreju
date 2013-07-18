/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
