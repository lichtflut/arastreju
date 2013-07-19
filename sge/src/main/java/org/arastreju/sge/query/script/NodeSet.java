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
package org.arastreju.sge.query.script;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.traverse.Walker;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.arastreju.sge.SNOPS.id;
import static org.arastreju.sge.SNOPS.qualify;

/**
 * <p>
 *  Set of nodes to operate on on script.
 * </p>
 *
 * <p>
 *  Created July 18, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class NodeSet {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeSet.class);

    private final ScriptEngineContext ctx;

    private Collection<SemanticNode> nodes;

    // ----------------------------------------------------

    public NodeSet(Collection<SemanticNode> nodes, ScriptEngineContext ctx) {
        this.ctx = ctx;
        this.nodes = nodes;
    }

    public NodeSet(SemanticNode node, ScriptEngineContext ctx) {
        this.ctx = ctx;
        this.nodes = Collections.singleton(node);
    }

    // -- Methods exposed to Java Script ------------------

    public NodeSet walk(String predicate) {
        Walker walker = Walker.start(nodes);
        walker.walk(id(qualify(predicate)));
        LOGGER.debug("Walked along '{}'. New node set: {}", predicate, walker.all());
        return new NodeSet(walker.all(), ctx);
    }

    public NodeSet filter(org.mozilla.javascript.Function f) {
        List<SemanticNode> filtered = new ArrayList<>();
        Scriptable scope = f.getParentScope();
        Scriptable thisObj = f.getParentScope();
        Context context = Context.getCurrentContext();
        for (SemanticNode node : nodes) {
            Object result = f.call(context, scope, thisObj,
                    new Object[]{new NodeSet(node, ctx)});
            if (Boolean.TRUE.equals(result)) {
                filtered.add(node);
            }
        }
        LOGGER.debug("Filtered nodes: {}", filtered);
        return new NodeSet(filtered, ctx);
    }

    public boolean matches(String value) {
        LOGGER.debug("Matching against a value: {}", value);
        if (value == null) {
            return false;
        }
        value = value.trim();
        // Redundant implementation to spare collection instantiation
        for (SemanticNode node : nodes) {
            if (hasValue(node, value)) {
                return true;
            }
            if (node.isResourceNode()) {
                for (Statement stmt : node.asResource().getAssociations()) {
                    if (hasValue(stmt.getObject(), value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean matches(String key, String value) {
        if (value == null || key == null) {
            return false;
        }
        LOGGER.info("Matching attributes with key {} against a value: {}", key, value);
        value = value.trim();
        ResourceID predicate = id(qualify(key));
        // Redundant implementation to spare collection instantiation
        for (SemanticNode node : nodes) {
            if (node.isResourceNode()) {
                for (Statement stmt : node.asResource().getAssociations()) {
                    if (stmt.getPredicate().equals(predicate) && hasValue(stmt.getObject(), value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public SemanticNode[] nodes() {
        return nodes.toArray(new SemanticNode[nodes.size()]);
    }

    public void result() {
        ctx.result(this);
    }

    // ----------------------------------------------------

    @Override
    public String toString() {
        return "NodeSet" + nodes;
    }

    // ----------------------------------------------------

    private boolean hasValue(SemanticNode object, String value) {
        if (object.isResourceNode()) {
            return object.asResource().toURI().equals(value);
        } else {
            return object.asValue().toString().equals(value);
        }
    }

}
