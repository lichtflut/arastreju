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

import org.arastreju.sge.query.QueryResult;
import org.arastreju.sge.query.SortCriteria;

/**
 * <p>
 *  Searcher for the Arastreju index.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface IndexSearcher {

    /**
     * Search the index.
     *
     * @param query The lucene query.
     * @param sort The sort criteria.
     * @return An iterator over the search results.
     */
    DynamicIndexSearch search(String query, SortCriteria sort);

}
