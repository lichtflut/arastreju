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
import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.AssociationResolver;
import org.arastreju.sge.spi.AssociationWriter;
import org.arastreju.sge.spi.ConversationController;
import org.arastreju.sge.spi.GraphDataStore;
import org.arastreju.sge.spi.tx.TxProvider;
import org.arastreju.sge.spi.util.FileStoreUtil;

import java.io.IOException;
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

    private final IndexProvider indexProvider;

    // ----------------------------------------------------

    public MemStorage() throws IOException {
        this(new IndexProvider(FileStoreUtil.prepareTempStore()));
    }

    public MemStorage(IndexProvider indexProvider) {
        this.indexProvider = indexProvider;
    }

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
    public AssociationResolver createAssociationResolver(ConversationController ctx) {
        return new MemAssociationResolver(ctx, this);
    }

    @Override
    public AssociationWriter crateAssociationWriter(ConversationController ctx) {
        return new MemAssociationWriter(ctx.getConversationContext(), this);
    }

    @Override
    public TxProvider createTxProvider(ConversationController ctx) {
        return new MemTransactionProvider();
    }

    @Override
    public IndexProvider getIndexProvider() {
        return indexProvider;
    }

    @Override
    public void close() {
        store.clear();
    }

    // ----------------------------------------------------

    StoredResource getStoreEntry(QualifiedName qn) {
        return store.get(qn);
    }
}
