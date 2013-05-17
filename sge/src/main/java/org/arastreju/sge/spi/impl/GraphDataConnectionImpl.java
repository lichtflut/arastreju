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

import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.AssociationResolver;
import org.arastreju.sge.spi.AssociationWriter;
import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.GraphDataStore;
import org.arastreju.sge.spi.ConversationController;
import org.arastreju.sge.spi.tx.TxProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Abstract base of graph data connections.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class GraphDataConnectionImpl implements GraphDataConnection {

    private final Set<ConversationController> openConversations = new HashSet<ConversationController>();

    private final GraphDataStore store;

    // ----------------------------------------------------

    /**
     * Constructor.
     * @param store The physical store.
     */
    public GraphDataConnectionImpl(GraphDataStore store) {
        this.store = store;
    }

    // ----------------------------------------------------

    @Override
    public AttachedAssociationKeeper find(QualifiedName qn) {
        return store.find(qn);
    }

    @Override
    public AttachedAssociationKeeper create(QualifiedName qn) {
        return store.create(qn);
    }

    @Override
    public void remove(QualifiedName qn) {
        store.remove(qn);
    }

    // ----------------------------------------------------

    @Override
    public AssociationResolver createAssociationResolver(ConversationController ctx) {
        return store.createAssociationResolver(ctx);
    }

    @Override
    public AssociationWriter createAssociationWriter(ConversationController ctx) {
        return store.crateAssociationWriter(ctx);
    }

    // ----------------------------------------------------

    @Override
    public void register(ConversationController conversationContext) {
        openConversations.add(conversationContext);
    }

    @Override
    public void unregister(ConversationController conversationContext) {
        openConversations.remove(conversationContext);
    }

    /**
     * Called when a resource has been modified by conversation context belonging to this graph data connection.
     * @param qn The qualified name of the modified resource.
     * @param context The context, where the modification occurred.
     */
    @Override
    public void notifyModification(QualifiedName qn, ConversationController context) {
        List<ConversationController> copy = new ArrayList<ConversationController>(openConversations);
        for (ConversationController current : copy) {
            if (!current.equals(context)) {
                current.onModification(qn, context);
            }
        }
    }

    /**
     * Close the connection and free all resources.
     */
    @Override
    public void close() {
        List<ConversationController> copy = new ArrayList<ConversationController>(openConversations);
        // iterating over copy because original will be remove itself while closing.
        for (ConversationController cc : copy) {
            cc.close();
        }
    }

    // ----------------------------------------------------

    @Override
    public IndexProvider getIndexProvider() {
        return store.getIndexProvider();
    }

    @Override
    public TxProvider createTxProvider(ConversationController ctx) {
        return store.createTxProvider(ctx);
    }
}
