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

import org.arastreju.sge.spi.WorkingContext;

/**
 * <p>
 *  Abstract base class for (bound) transaction controls.
 * </p>
 *
 * <p>
 *  Created Feb. 24, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractTransactionControl implements BoundTransactionControl {

    private WorkingContext ctx;

    // ----------------------------------------------------

    @Override
    public void commit() {
        success();
        finish();
    }

    @Override
    public void rollback() {
        fail();
        finish();
    }

    // ----------------------------------------------------

    @Override
    public BoundTransactionControl bind(WorkingContext ctx) {
        this.ctx = ctx;
        return this;
    }

    @Override
    public WorkingContext getContext() {
        return ctx;
    }

    // ----------------------------------------------------

    protected void assertTxActive() {
        if (!isActive()) {
            throw new IllegalStateException("Transaction has already been closed.");
        }
    }

}
