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

import org.apache.lucene.search.TopDocs;
import org.arastreju.sge.naming.QualifiedName;

import java.util.Iterator;

/**
 * <p>
 *  Represents the result of a search using IndexSearcher.
 * </p>
 *
 * <p>
 *  Created 12.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface IndexSearchResult extends Iterable<QualifiedName> {

    /**
     * Close the result object.
     * Should always be called to free resources.
     */
    void close();

    /**
     * Returns the size of the result entries or -1 if the size is unknown.
     * @return The size.
     */
    int size();

}
