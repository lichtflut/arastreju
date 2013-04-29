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
package org.arastreju.bindings.memory.tx;

import org.arastreju.sge.spi.tx.AbstractTxProvider;
import org.arastreju.sge.spi.tx.BoundTransactionControl;
import org.arastreju.sge.spi.tx.PseudoTransaction;

/**
 * <p>
 *  In-Memory specific TX provider.
 * </p>
 *
 * <p>
 *  Created 20.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemTransactionProvider extends AbstractTxProvider {

    @Override
    protected BoundTransactionControl newTx() {
        return new PseudoTransaction();
    }

}
