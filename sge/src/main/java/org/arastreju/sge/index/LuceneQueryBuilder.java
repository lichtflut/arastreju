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
package org.arastreju.sge.index;

import de.lichtflut.infra.exceptions.NotYetSupportedException;
import org.arastreju.sge.query.QueryBuilder;
import org.arastreju.sge.query.QueryException;
import org.arastreju.sge.query.QueryExpression;
import org.arastreju.sge.query.QueryOperator;
import org.arastreju.sge.query.QueryParam;
import org.arastreju.sge.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

/**
 * <p>
 *  Lucene specific query builder.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class LuceneQueryBuilder extends QueryBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneQueryBuilder.class);

    private final IndexSearcher searcher;

    private final QNResolver resolver;

    // -----------------------------------------------------

    /**
     * Constructor.
     * @param searcher The index searcher.
     * @param resolver The resolver for qualified names.
     */
    public LuceneQueryBuilder(IndexSearcher searcher, QNResolver resolver) {
        this.searcher = searcher;
        this.resolver = resolver;
    }

    // -----------------------------------------------------

    @Override
    public QueryResult getResult() {
        final IndexSearchResult hits = searcher.search(toQueryString());
        return new LuceneQueryResult(hits, resolver);
    }

    // -----------------------------------------------------

    /**
     * Return the Lucence query.
     * @return The query.
     */
    protected String toQueryString() {
        final StringBuilder sb = new StringBuilder();
        append(getRoot(), sb);
        return sb.toString();
    }

    @Override
    protected QueryExpression getRoot() {
        return super.getRoot();
    }

    // -----------------------------------------------------

    private void append(final QueryExpression exp, final StringBuilder sb) {
        if (exp.isLeaf()) {
            appendLeaf(exp.getQueryParam(), sb);
        } else {
            if (QueryOperator.NOT.equals(exp.getOperator())) {
                sb.append(" ").append(exp.getOperator().name()).append(" ");
            }
            sb.append("(");
            boolean first = true;
            for (QueryExpression child : exp.getChildren()) {
                if (first) {
                    first = false;
                } else if (!QueryOperator.NOT.equals(exp.getOperator())) {
                    sb.append(" ").append(exp.getOperator().name()).append(" ");
                }
                append(child, sb);
            }
            sb.append(")");
        }
    }

    private void appendLeaf(final QueryParam param, final StringBuilder sb) {
        String value = normalizeValue(param.getValue());
        if (value == null || value.length() == 0) {
            throw new QueryException("Invalid query value: " + param);
        }
        switch(param.getOperator()) {
            case EQUALS:
                sb.append(normalizeKey(param.getName())).append(":");
                break;
            case HAS_URI:
                sb.append(IndexFields.QUALIFIED_NAME + ":");
                break;
            case HAS_VALUE:
                sb.append(IndexFields.RESOURCE_VALUE + ":");
                break;
            case HAS_RELATION:
                sb.append(IndexFields.RESOURCE_RELATION + ":");
                break;
            case SUB_QUERY:
                sb.append(param.getValue());
                // abort here!
                return;
            default:
                throw new NotYetSupportedException(param.getOperator());
        }
        sb.append(value);
    }

    private String normalizeKey(final String key) {
        return key.replaceAll(":", Matcher.quoteReplacement("\\:"));
    }

    private String normalizeValue(final Object value) {
        if (value == null) {
            return null;
        }
        String normalized = value.toString().trim().toLowerCase();
        if (normalized.contains(" ")) {
            normalized = "\"" + normalized + "\"";
        }
        return normalized.replaceAll(":", Matcher.quoteReplacement("\\:"));
    }

}
