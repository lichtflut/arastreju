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
import org.arastreju.sge.spi.ConversationController;

/**
 * <p>
 *  Provider of transactions.
 * </p>
 *
 * <p>
 * 	Created Jun 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractTxProvider implements TxProvider {

	private BoundTransactionControl tx;

	// -----------------------------------------------------

	/**
	 * Constructor.
	 */
	public AbstractTxProvider() {
	}
	
	// -----------------------------------------------------
	
	@Override
    public BoundTransactionControl begin() {
		if (!inTransaction()) {
			tx = newTx();
			return tx;
		} else {
			return newSubTx(tx);
		}
	}
	
	@Override
    public boolean inTransaction() {
		return tx != null && tx.isActive();
	}
	
	// -----------------------------------------------------
	
	@Override
    public void doTransacted(final TxAction action, ConversationController ctx){
		final BoundTransactionControl tx = begin();
        tx.bind(ctx);
		try {
			action.execute();
			tx.success();
		} catch (RuntimeException e) {
			e.printStackTrace();
			tx.fail();
			throw e;
		} finally {
			tx.finish();
		}
	}
	
	@Override
    public <T> T doTransacted(final TxResultAction<T> action, ConversationController ctx){
		final BoundTransactionControl tx = begin();
        tx.bind(ctx);
		try {
			T result = action.execute();
			tx.success();
			return result;
		} catch (RuntimeException e) {
			e.printStackTrace();
			tx.fail();
			throw e;
		} finally {
			tx.finish();
		}
	}

    // ----------------------------------------------------

    protected abstract BoundTransactionControl newTx();

    protected BoundTransactionControl newSubTx(BoundTransactionControl parent) {
        return new SubTransaction(parent);
    }

}
