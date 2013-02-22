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
package org.arastreju.sge.persistence;

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
public abstract class TxProvider {

	private TransactionControl tx;

	// -----------------------------------------------------

	/**
	 * Constructor.
	 */
	public TxProvider() {
	}
	
	// -----------------------------------------------------
	
	/**
	 * Begin a new transaction if not already one open.
	 * @return An active transaction.
	 */
	public TransactionControl begin() {
		if (!inTransaction()) {
			tx = newTx();
			return tx;
		} else {
			return newSubTx(tx);
		}
	}
	
	/**
	 * Check if there is a transaction running.
	 * @return true if there is a transaction.
	 */
	public boolean inTransaction() {
		return tx != null && tx.isActive();
	}
	
	// -----------------------------------------------------
	
	public void doTransacted(final TxAction action){
		final TransactionControl tx = begin();
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
	
	public <T> T doTransacted(final TxResultAction<T> action){
		final TransactionControl tx = begin();
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

    protected abstract TransactionControl newTx();

    protected TransactionControl newSubTx(TransactionControl parent) {
    	return new SubTransaction(parent);
    }
	
}
