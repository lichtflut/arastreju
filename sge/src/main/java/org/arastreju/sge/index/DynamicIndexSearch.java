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

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.naming.QualifiedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  Dynamic search for lucene.
 * </p>
 *
 * <p>
 *  Created 22 July, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class DynamicIndexSearch {

    public static final int DEFAULT_FETCH_SIZE = 50;

    // ----------------------------------------------------

    private final Query query;

    private final Sort sort;

    private final ContextIndex contextIndex;

    // ----------------------------------------------------

    private TopDocs hits;

    private int read;

    // ----------------------------------------------------

    public DynamicIndexSearch(Query query, Sort sort, ContextIndex contextIndex) {
        this.query = query;
        this.sort = sort;
        this.contextIndex = contextIndex;
    }

    // ----------------------------------------------------

    public int size() {
        if (hits == null) {
            read(DEFAULT_FETCH_SIZE);
        }
        return hits.totalHits;
    }

    // ----------------------------------------------------

    public List<QualifiedName> toList(int offset, int max) {
        try (IndexReader reader = contextIndex.createReader()) {

            read(offset + max, reader);

            int last = Math.min(offset + max, size());
            if (offset >= last) {
                return Collections.emptyList();
            }
            final List<QualifiedName> result = new ArrayList<>(last - offset);
            for (int i=offset; i < last; i++) {
                Document document = reader.document(hits.scoreDocs[i].doc);
                result.add(QualifiedName.from(document.get(IndexFields.QUALIFIED_NAME)));
            }
            return result;

        } catch (IOException e) {
            throw new ArastrejuRuntimeException(ErrorCodes.QUERY_IO_ERROR, "Query failed.");
        }

    }

    // ----------------------------------------------------

    private void read(int max) {
        if (read >= max) {
            // nothing to do
            return;
        }

        try (IndexReader reader = contextIndex.createReader()) {
            read(max, reader);
        } catch (IOException e) {
            throw new ArastrejuRuntimeException(ErrorCodes.QUERY_IO_ERROR, "Query failed.");
        }
    }

    private void read(int max, IndexReader reader) {
        if (read >= max) {
            // nothing to do
            return;
        }

        try {
            org.apache.lucene.search.IndexSearcher searcher =
                    new org.apache.lucene.search.IndexSearcher(reader);

            if (sort != null && sort.getSort().length > 0) {
                hits = searcher.search(query, null, max, sort);
            } else {
                hits = searcher.search(query, null, max);
            }
            read = max;
            searcher.close();
        } catch (IOException e) {
            throw new ArastrejuRuntimeException(ErrorCodes.QUERY_IO_ERROR, "Query failed.");
        }
    }

}
