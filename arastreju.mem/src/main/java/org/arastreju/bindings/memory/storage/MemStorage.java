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

import org.arastreju.bindings.memory.tx.MemTransactionProvider;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.AssociationResolver;
import org.arastreju.sge.spi.AssociationWriter;
import org.arastreju.sge.spi.GraphDataStore;
import org.arastreju.sge.spi.WorkingContext;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  The in memory database for resource nodes.
 * </p>
 *
 * <p>
 *  Created 25.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemStorage implements GraphDataStore {

    private final Map<QualifiedName, StoredResource> store = new HashMap<QualifiedName, StoredResource>();

    // ----------------------------------------------------

    @Override
    public AttachedAssociationKeeper find(QualifiedName qn) {
        StoredResource storedResource = store.get(qn);
        if (storedResource != null) {
            return new AttachedAssociationKeeper(qn, storedResource.getId(), storedResource.getStatements());
        } else {
            return null;
        }
    }

    @Override
    public AttachedAssociationKeeper create(QualifiedName qn) {
        StoredResource storedResource = new StoredResource(qn);
        store.put(qn, storedResource);
        return new AttachedAssociationKeeper(qn, storedResource.getId());
    }

    @Override
    public void remove(QualifiedName qn) {
        store.remove(qn);
    }

    @Override
    public AssociationResolver createAssociationResolver(WorkingContext ctx) {
        return new MemAssociationResolver(ctx, this);
    }

    @Override
    public AssociationWriter crateAssociationWriter(WorkingContext ctx) {
        return new MemAssociationWriter(ctx, this);
    }

    @Override
    public TxProvider getTxProvider() {
        return new MemTransactionProvider();
    }

    // ----------------------------------------------------

    StoredResource getStoreEntry(QualifiedName qn) {
        return store.get(qn);
    }
}
