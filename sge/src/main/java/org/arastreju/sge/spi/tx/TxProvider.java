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
package org.arastreju.sge.spi.tx;

import org.arastreju.sge.persistence.TxAction;
import org.arastreju.sge.persistence.TxResultAction;
import org.arastreju.sge.spi.WorkingContext;

/**
 * <p>
 *  Provider of transactions.
 * </p>
 *
 * <p>
 *  Created Feb. 24, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public interface TxProvider {

    /**
     * Begin a new transaction if not already one open.
     * @return An active transaction.
     */
    BoundTransactionControl begin();

    /**
     * Check if there is a transaction running.
     * @return true if there is a transaction.
     */
    boolean inTransaction();

    /**
     * Perform given action in a transaction.
     * @param action The action.
     * @param ctx The context to bind the transaction to.
     */
    void doTransacted(TxAction action, WorkingContext ctx);

    /**
     * Perform given action in a transaction.
     * @param action The action.
     * @param ctx The context to bind the transaction to.
     */
    <T> T doTransacted(TxResultAction<T> action, WorkingContext ctx);

}
