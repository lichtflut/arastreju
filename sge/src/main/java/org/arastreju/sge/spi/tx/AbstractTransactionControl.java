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

import org.arastreju.sge.spi.ConversationController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private List<TxListener> listeners = new ArrayList<TxListener>();

    private ConversationController ctx;

    private boolean success;

    private boolean fail;

    // ----------------------------------------------------

    @Override
    public AbstractTransactionControl register(TxListener... listeners) {
        Collections.addAll(this.listeners, listeners);
        return this;
    }

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
    public final void success() {
        assertTxActive();
        this.success = true;
        onSuccess();
    }

    @Override
    public final void fail() {
        assertTxActive();
        this.fail = true;
        onFail();
    }

    @Override
    public final void finish() {
        assertTxActive();
        if (success) {
            notifyBeforeCommit();
            onFinish();
            notifyAfterCommit();
        } else {
            notifyRollback();
            onFinish();
        }
    }

    @Override
    public BoundTransactionControl bind(ConversationController ctx) {
        this.ctx = ctx;
        ctx.beginUnitOfWork(this);
        return this;
    }

    @Override
    public ConversationController getContext() {
        return ctx;
    }

    // -- abstracts ---------------------------------------

    protected abstract void onSuccess();

    protected abstract void onFail();

    protected abstract void onFinish();

    // ----------------------------------------------------

    protected void notifyBeforeCommit() {
        for (TxListener listener : listeners) {
            listener.onBeforeCommit();
        }
    }

    protected void notifyAfterCommit() {
        for (TxListener listener : listeners) {
            listener.onAfterCommit();
        }
    }

    protected void notifyRollback() {
        for (TxListener listener : listeners) {
            listener.onRollback();
        }
    }

    protected void assertTxActive() {
        if (!isActive()) {
            throw new IllegalStateException("Transaction has already been closed.");
        }
    }

}
