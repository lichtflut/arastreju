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

import org.arastreju.sge.Conversation;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryParser;
import org.arastreju.sge.query.QueryResult;
import org.arastreju.sge.query.SimpleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  Context available from query java scripts. Instances of this class can be 'compiled' to JavaScript
 *  and added to script contexts.
 * </p>
 *
 * <p>
 *  Created 18.07.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class ScriptEngineContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptEngineContext.class);

    private final Conversation conversation;

    private final List<ResourceNode> results = new ArrayList<>();

    // ----------------------------------------------------

    public ScriptEngineContext(Conversation conversation) {
        this.conversation = conversation;
    }

    // -- Methods exposed to Java Script ------------------

    public NodeSet query(String queryString) {
        LOGGER.debug("Performing query from script: {}", queryString);
        Query query = conversation.createQuery();
        new QueryParser().adapt(query, queryString);
        NodeSet nodeSet = new NodeSet((Collection) query.getResult().toList(5000), this);
        LOGGER.debug("Query result: {}", nodeSet);
        return nodeSet;
    }

    // ----------------------------------------------------

    protected void result(NodeSet result) {
        LOGGER.debug("Got result: {}", result);
        for (SemanticNode node : result.nodes()) {
            if (node.isResourceNode()) {
                this.results.add(node.asResource());
            }
        }
    }

    protected QueryResult getQueryResult() {
        return new SimpleQueryResult(results);
    }

}
