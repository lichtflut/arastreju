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

import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

import static org.arastreju.sge.SNOPS.qualify;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 *  Test cases for query script.
 * </p>
 *
 * <p>
 *  Created July 18, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class QueryScriptEngineTest {

    @Test
    public void shouldCallArasContext() throws QueryScriptException {
        final String query = "'x'='y'";
        final String walkPredicate = "rdf:type";

        ScriptEngineContext ctx = mock(ScriptEngineContext.class);
        NodeSet nodeSet = mock(NodeSet.class);

        when(ctx.query(anyString())).thenReturn(nodeSet);
        when(nodeSet.walk(anyString())).thenReturn(nodeSet);

        QueryScriptEngine engine = new QueryScriptEngine(ctx);
        engine.execute(
                "query(\"" +  query + "\")" +
                "   .walk('" + walkPredicate + "')" +
                        ".result();");

        verify(ctx).query(Mockito.eq(query));
        verify(nodeSet).walk(Mockito.eq(walkPredicate));
        verify(nodeSet).result();
    }

    // ----------------------------------------------------

    @Test
    public void testFiltering() throws QueryScriptException {

        ScriptEngineContext ctx = mock(ScriptEngineContext.class);

        SNResource a = new SNResource(qualify("local:a"));
        SNResource b = new SNResource(qualify("local:b"));
        SNResource c = new SNResource(qualify("local:c"));

        Collection<SemanticNode> initial = Arrays.<SemanticNode>asList(a, b, c);
        NodeSet nodeSet = new NodeSet(initial, ctx);

        when(ctx.query(anyString())).thenReturn(nodeSet);

        QueryScriptEngine engine = new QueryScriptEngine(ctx);
        engine.execute("query(\"'x'='y'\").filter(" +
                "function (e) { " +
                "   return e.getQualifiedName() == 'local:b'; " +
                "}).result();");


        ArgumentCaptor<NodeSet> resultNS = ArgumentCaptor.forClass(NodeSet.class);
        verify(ctx).result(resultNS.capture());

        SemanticNode[] filtered = resultNS.getValue().nodes();

        Assert.assertTrue(filtered.length == 1);
        Assert.assertEquals("local:b", filtered[0].asResource().toURI());

    }

}
