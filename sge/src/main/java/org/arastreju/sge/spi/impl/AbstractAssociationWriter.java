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
package org.arastreju.sge.spi.impl;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.AssociationWriter;
import org.arastreju.sge.spi.GraphDataStore;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  Abstract base for association writers.
 * </p>
 *
 * <p>
 *  Created Feb. 22, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractAssociationWriter implements AssociationWriter {

    private static final Context[] NO_CTX = new Context[0];

    private final ConversationContext convContext;

    private final GraphDataStore store;

    // ----------------------------------------------------

    public AbstractAssociationWriter(ConversationContext ctx, GraphDataStore store) {
        this.convContext = ctx;
        this.store = store;
    }

    // ----------------------------------------------------

    protected Context[] getCurrentContexts(Statement stmt) {
        if (stmt.getContexts().length == 0) {
            if (convContext.getPrimaryContext() == null) {
                return NO_CTX;
            } else {
                return new Context[] { convContext.getPrimaryContext() };
            }
        } else if (convContext.getPrimaryContext() == null) {
            return stmt.getContexts();
        } else {
            Set<Context> joined = new HashSet<Context>();
            joined.add(convContext.getPrimaryContext());
            Collections.addAll(joined, stmt.getContexts());
            return joined.toArray(new Context[joined.size()]);
        }
    }

    protected boolean exists(QualifiedName qn) {
        return store.find(qn) != null;
    }

    protected void assureExists(QualifiedName qn) {
        if (!exists(qn)) {
            store.create(qn);
        }
    }

}