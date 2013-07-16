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
package org.arastreju.sge.structure;

import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Utility class to operate on tree structures.
 * </p>
 *
 * <p>
 *  Created July 16, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class TreeStructure {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeStructure.class);

    // ----------------------------------------------------

    public static List<ResourceNode> children(ResourceNode parent) {
        ArrayList<ResourceNode> children = new ArrayList<>();
        for (Statement stmt : parent.getAssociations()) {
            SNProperty predicate = SNProperty.from(stmt.getPredicate());
            if (!predicate.isAttached()) {
                LOGGER.warn("Property {} is not attached. Con not derive super properties.", predicate);
            }
            if (stmt.getObject().isResourceNode() && predicate.isSubPropertyOf(Aras.HAS_CHILD_NODE)) {
                children.add(stmt.getObject().asResource());
            }
        }
        return children;
    }
}
