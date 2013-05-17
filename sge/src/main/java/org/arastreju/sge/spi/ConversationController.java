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

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.index.ConversationIndex;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.tx.BoundTransactionControl;
import org.arastreju.sge.spi.tx.TxProvider;

/**
 * <p>
 *  The working context of a conversation.
 * </p>
 *
 * <p>
 *  Created 27.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ConversationController {

    /**
     * Lookup the qualified name in the register.
     * @param qn The qualified name.
     * @return The association keeper or null.
     */
    AttachedAssociationKeeper lookup(QualifiedName qn);

    /**
     * Find the resource in this conversation context or in underlying data store.
     * @param qn The resource's qualified name.
     * @return The association keeper or null.
     */
    AttachedAssociationKeeper find(QualifiedName qn);

    /**
     * Create a new resource.
     * @param qn The resource's qualified name.
     * @return The attached association keeper or null;
     */
    AttachedAssociationKeeper create(QualifiedName qn);

    /**
     * Remove the resource identified by given qualified name.
     * @param qn The resource's qualified name.
     */
    void remove(QualifiedName qn);

    /**
     * Attach the keeper to this context. The resource will be created if it does not exist yet.
     * @param qn The resource's qualified name.
     * @param keeper The keeper to be accessed.
     */
    void attach(QualifiedName qn, AttachedAssociationKeeper keeper);

    /**
     * Detach the keeper from the context.
     * @param qn The resource's qualified name.
     */
    void detach(QualifiedName qn);

    // ----------------------------------------------------

    /**
     * Add a new Association to given keeper and store it.
     * @param associationKeeper The keeper, which shall be the subject in the new association.
     * @param assoc The Association.
     */
    void addAssociation(AttachedAssociationKeeper associationKeeper, Statement assoc);

    /**
    * Remove the given association.
    * @param associationKeeper The keeper.
    * @param assoc The association.
    * @return true if the association has been removed.
    */
    boolean removeAssociation(AttachedAssociationKeeper associationKeeper, Statement assoc);

    /**
     * Resolve the associations of given association keeper.
     * @param associationKeeper The association keeper to be resolved.
     */
    void resolveAssociations(AttachedAssociationKeeper associationKeeper);

    // ----------------------------------------------------

    /**
     * Get the context of the conversation.
     * @return The context.
     */
    ConversationContext getConversationContext();

    /**
     * Get the index access for this conversation.
     * @return The index.
     */
    ConversationIndex getIndex();

    /**
     * Get the provider for transactions.
     * @return The transaction provider.
     */
    TxProvider getTxProvider();

    /**
     * Begin a new Unit of Work.
     * @param tx The transaction for this UoW.
     */
    void beginUnitOfWork(BoundTransactionControl tx);

    // ----------------------------------------------------

    /**
     * Close this context and clear all registered resources.
     */
    void close();

    /**
     * Check if the conversation is still active.
     * @return true if conversation is active.
     */
    boolean isActive();

    /**
     * Called when a resource has been modified by another conversation context with same graph data connection.
     * @param qn The qualified name of the modified resource.
     * @param otherContext The other context, where the modification occurred.
     */
    void onModification(QualifiedName qn, ConversationController otherContext);

}
