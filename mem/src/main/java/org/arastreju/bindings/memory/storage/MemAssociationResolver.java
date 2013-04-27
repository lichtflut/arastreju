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

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.spi.AssociationResolver;
import org.arastreju.sge.spi.WorkingContext;

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
public class MemAssociationResolver implements AssociationResolver {

    private WorkingContext ctx;

    private MemStorage storage;

    // ----------------------------------------------------

    public MemAssociationResolver(WorkingContext ctx, MemStorage storage) {
        this.ctx = ctx;
        this.storage = storage;
    }

    // ----------------------------------------------------

    @Override
    public void resolveAssociations(AttachedAssociationKeeper keeper) {
        StoredResource entry = storage.getStoreEntry(keeper.getQualifiedName());
        for (Statement statement : entry.getStatements()) {
            keeper.addAssociationDirectly(statement);
        }
    }

}
