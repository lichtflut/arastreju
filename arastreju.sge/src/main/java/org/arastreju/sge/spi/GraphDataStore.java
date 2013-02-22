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

import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;

/**
 * <p>
 *  Wrapper of the physical data store.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface GraphDataStore {

    /**
     * Find the resource with given qualified name and wrap it in a new association keeper object.
     * @param qn The qualified name.
     * @return The association keeper or null if not found.
     */
    AttachedAssociationKeeper find(QualifiedName qn);

    /**
     * Create a new resource with given qualified name.
     * @param qn The qualified name.
     * @return The new association keeper.
     */
    AttachedAssociationKeeper create(QualifiedName qn);

    /**
     * Remove the resource identified by qualified name from the store.
     * @param qn The qualified name.
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
    AssociationWriter crateAssociationWriter(WorkingContext ctx);

    // ----------------------------------------------------

    /**
     * Get the provider for transactions.
     * @return The transaction provider.
     */
    TxProvider getTxProvider();

    /**
     * Close the store.
     */
    void close();

}
