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
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.QueryResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  Result of a lucene query.
 *
 *  TODO: support for closing of lucene hits.
 *
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class LuceneQueryResult implements QueryResult {

    private final IndexSearchResult hits;

    private final QNResolver resolver;

    // ----------------------------------------------------

    public LuceneQueryResult(IndexSearchResult indexHits, QNResolver resolver) {
        this.hits = indexHits;
        this.resolver = resolver;
    }

    // ----------------------------------------------------

    @Override
    public void close() {
        throw new NotYetSupportedException();
    }

    @Override
    public int size() {
        return hits.size();
    }

    @Override
    public List<ResourceNode> toList() {
        final List<ResourceNode> result = new ArrayList<ResourceNode>();
        for (QualifiedName node : hits) {
            result.add(resolver.resolve(node));
        }
        return result;
    }

    @Override
    public List<ResourceNode> toList(int max) {
        final List<ResourceNode> result = new ArrayList<ResourceNode>(max);
        final Iterator<QualifiedName> iterator = hits.iterator();
        for (int i=0; i < max; i++) {
            if (!iterator.hasNext()) {
                break;
            }
            result.add(resolver.resolve(iterator.next()));
        }
        return result;
    }

    @Override
    public List<ResourceNode> toList(int offset, int max) {
        if (offset >= max) {
            return Collections.emptyList();
        }
        final List<ResourceNode> result = new ArrayList<ResourceNode>(max - offset);
        final Iterator<QualifiedName> iterator = hits.iterator();
        for (int i=0; i < max; i++) {
            final QualifiedName next = iterator.next();
            if (i >= offset) {
                result.add(resolver.resolve(next));
            }
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return !hits.iterator().hasNext();
    }

    @Override
    public ResourceNode getSingleNode() {
        return iterator().next();
    }

    @Override
    public Iterator<ResourceNode> iterator() {
        final Iterator<QualifiedName> iterator = hits.iterator();
        return new Iterator<ResourceNode>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ResourceNode next() {
                return resolver.resolve(iterator.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
