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
