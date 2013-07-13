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
package org.arastreju.bindings.memory.index;

import org.arastreju.sge.eh.meta.NotYetImplementedException;
import org.arastreju.sge.index.IndexSearchResult;
import org.arastreju.sge.index.IndexSearcher;

/**
 * <p>
 *  Temporary mock until lucene implementation is available.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class MockIndex implements IndexSearcher {

    @Override
    public IndexSearchResult search(String query) {
        throw new NotYetImplementedException();
    }

}
