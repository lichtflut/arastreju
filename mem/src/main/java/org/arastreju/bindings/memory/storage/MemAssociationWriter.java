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
package org.arastreju.bindings.memory.storage;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.spi.AssociationWriter;
import org.arastreju.sge.spi.WorkingContext;
import org.arastreju.sge.spi.impl.AbstractAssociationWriter;

/**
 * <p>
 *  Mem binding specific implementation of association resolver.
 * </p>
 *
 * <p>
 *  Created Feb. 22, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemAssociationWriter extends AbstractAssociationWriter {

    private MemStorage storage;

    // ----------------------------------------------------

    public MemAssociationWriter(WorkingContext ctx, MemStorage storage) {
        super(ctx, storage);
        this.storage = storage;
    }

    // ----------------------------------------------------

    @Override
    public void onCreate(Statement stmt) {
        StoredResource entry = storage.getStoreEntry(stmt.getSubject().getQualifiedName());
        entry.addAssociation(copyForStorage(stmt));
    }

    @Override
    public void onRemove(Statement stmt) {
        StoredResource entry = storage.getStoreEntry(stmt.getSubject().getQualifiedName());
        entry.remove(stmt);
    }

    // ----------------------------------------------------

    private Statement copyForStorage(Statement original) {
        Context[] ctxs = getCurrentContexts(original);
        return new DetachedStatement(original.getSubject(), original.getPredicate(), original.getObject(), ctxs);
    }
}
