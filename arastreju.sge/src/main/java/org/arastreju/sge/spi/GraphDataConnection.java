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
package org.arastreju.sge.spi;

import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.tx.TxProvider;

/**
 * <p>
 *  The connection to a graph data store in the scope of a conversation.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface GraphDataConnection {

    /**
     * Find a resource.
     * @param qn The resource's qualified name.
     * @return The association keeper or null.
     */
    AttachedAssociationKeeper find(QualifiedName qn);

    /**
     * Create a new resource.
     * @param qn The resource's qualified name.
     * @return The new association keeper.
     */
    AttachedAssociationKeeper create(QualifiedName qn);

    /**
     * Remove a resource.
     * @param qn The resource's qualified name.
     */
    void remove(QualifiedName qn);

    // ----------------------------------------------------

    /**
     * Create a new association resolver for given working context.
     * @param ctx The context of the new resolver.
     * @return The resolver.
     */
    AssociationResolver createAssociationResolver(WorkingContext ctx);

    /**
     * Create a new association writer for given working context.
     * @param ctx The context of the new writer.
     * @return The writer.
     */
    AssociationWriter createAssociationWriter(WorkingContext ctx);

    // ----------------------------------------------------

    /**
     * Get the index provider.
     * @return Thr provider for indexes.
     */
    IndexProvider getIndexProvider();

    /**
     * Get the provider for transactions.
     * @return The transaction provider.
     */
    TxProvider getTxProvider();

    // ----------------------------------------------------

    /**
     * Notify the connection about a change for the resource with given qualified name.
     * @param qn The qualified name.
     * @param context The context in which occurred the change.
     */
    void notifyModification(QualifiedName qn, WorkingContext context);

    void register(WorkingContext conversationContext);

    void unregister(WorkingContext conversationContext);

    void close();
}
