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

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.QueryResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  Result of a lucene query.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class DynamicQueryResult implements QueryResult {

    private final QNResolver resolver;

    private final DynamicIndexSearch search;

    // ----------------------------------------------------

    public DynamicQueryResult(DynamicIndexSearch search, QNResolver resolver) {
        this.search = search;
        this.resolver = resolver;
    }

    // ----------------------------------------------------

    @Override
    public int size() {
        return search.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    // ----------------------------------------------------

    @Override
    public List<ResourceNode> toList() {
        return toList(0, Integer.MAX_VALUE);
    }

    @Override
    public List<ResourceNode> toList(int max) {
        return toList(0, max);
    }

    @Override
    public List<ResourceNode> toList(int offset, int max) {
        List<QualifiedName> qns = search.toList(offset, max);
        final List<ResourceNode> result = new ArrayList<>(qns.size());
        for (QualifiedName qn : qns) {
            result.add(resolver.resolve(qn));
        }
        return result;
    }

    @Override
    public ResourceNode getSingleNode() {
        List<ResourceNode> list = toList(0, 1);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public Iterator<ResourceNode> iterator() {
        return toList().iterator();
    }

    // ----------------------------------------------------

    @Override
    public void close() {
    }
}
