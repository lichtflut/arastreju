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

import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.spi.ConversationController;

/**
 * <p>
 *  Extension of Transaction Control. Can be bound to a working context.
 * </p>
 *
 * <p>
 *  Created Feb. 24, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public interface BoundTransactionControl extends TransactionControl {

    /**
     * Bind this transaction to given context.
     * @param ctx The context.
     * @return This.
     */
    BoundTransactionControl bind(ConversationController ctx);

    /**
     * Get the working context.
     * @return The context.
     */
    ConversationController getContext();

    // ----------------------------------------------------

    /**
     * Register a listener for events on this transaction.
     * @param listeners The listener(s) to register.
     * @return This.
     */
    BoundTransactionControl register(TxListener... listeners);

}
