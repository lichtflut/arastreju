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
package org.arastreju.sge.query.parser;

import junit.framework.Assert;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryBuilder;
import org.arastreju.sge.query.QueryParser;
import org.arastreju.sge.query.QueryResult;
import org.junit.Test;

import java.io.IOException;

/**
 * <p>
 *  Test case for query parsing.
 * </p>
 *
 * <p>
 *  Created 15.07.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class QueryParserTest {

    @Test
    public void testSimpleQuery() throws IOException {
        String qs = "'rdf:type' = 'Person'";
        Query query = new TestQueryBuilder();
        new QueryParser().adapt(query, qs);
        Assert.assertEquals("'rdf:type'='Person'", trim(query.toString()));
    }

    @Test
    public void testComplexQuery() throws IOException {
        String qs = "AND( NOT('rdf:type' = 'Person'), REL = 'y', OR (QN = 'v' , VAL = 'c'))";
        Query query = new TestQueryBuilder();
        new QueryParser().adapt(query, qs);
        Assert.assertEquals("AND(NOT('rdf:type'='Person'),REL='y',OR(QN='v',VAL='c'))",
                trim(query.toString()));
    }

    @Test
    public void testSpecialCharacter() throws IOException {
        String qs = "AND( 'http://www.w3.org/1999/02/22-rdf-syntax-ns#type' = 'http://ww$.example.org/Person?x=1&y=2#b' )";
        Query query = new TestQueryBuilder();
        new QueryParser().adapt(query, qs);
        Assert.assertEquals("AND('http://www.w3.org/1999/02/22-rdf-syntax-ns#type'='http://ww$.example.org/Person?x=1&y=2#b')",
                trim(query.toString()));
    }

    // ----------------------------------------------------

    private static class TestQueryBuilder extends QueryBuilder {
        @Override
        public QueryResult getResult() {
            throw new UnsupportedOperationException();
        }
    }

    private static String trim(String in) {
        return in.replaceAll(" ", "");
    }
}
