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

import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.ContextID;
import org.arastreju.sge.naming.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  Provider for indexes for a given context.
 * </p>
 *
 * <p>
 *  Created 12.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class IndexProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexProvider.class);

    private static final Context nullCtxDummy = ContextID.forContext(QualifiedName.generate());

    /* stores one index per context */
    private final Map<Context, ContextIndex> indexMap = new HashMap<Context, ContextIndex>();

    /* placeholder - to be turned into a proper configuration setting */
    private final String indexRoot;

    // ----------------------------------------------------

    public IndexProvider(String indexRootDir) {
        this.indexRoot = indexRootDir;
    }

    // ----------------------------------------------------

    /**
     * Obtain the index for this context.
     * @param context The context.
     * @return The corresponding index.
     */
    public ContextIndex forContext(Context context) {
        if (context == null)
            context = nullCtxDummy;
        ContextIndex index;
        synchronized (indexMap) {
            if ((index = indexMap.get(context)) == null) {
                try {
                    indexMap.put(context, (index = new ContextIndex(indexRoot, context)));
                } catch (IOException e) {
                    String msg = "caught IOException while creating index for context " + context.toURI();
                    LOGGER.error(msg, e);
                    throw new RuntimeException(msg, e);
                }
            }
        }

        return index;
    }

    /**
     * Release the index for this context.
     * @param context The context.
     */
    public void release(Context context) {
        synchronized (indexMap) {
            indexMap.remove(context == null ? nullCtxDummy : context);
        }
    }

    public void shutdown() {
        for (Context context : indexMap.keySet()) {
            try {
                indexMap.get(context).close();
            } catch (IOException e) {
                LOGGER.error("Failed to close context index.", e);
            }
        }
        indexMap.clear();
    }
}
