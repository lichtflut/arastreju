/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
 * 	Abstract class for transaction controlling of Arastreju gate.
 *  Knows its Transaction provider, accessible through tx().
 * </p>
 * 
 * <p>
 * 	Created: 10.07.2008
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class TransactionControl {
	private TxProvider tx;
	private boolean success;
	
	// ----------------------------------------------------
	
	public TransactionControl(TxProvider tx) {
		this.tx = tx;
		this.success = false;
	}
	
	// ----------------------------------------------------
	
	/**
	 * @return The transaction provider
	 */
	public TxProvider tx() {
		return tx;
	}
	
	/**
	 * Marks the transaction as successful. 
	 */
	public final void success() {
		success = true;
		onSuccess();
	}

	protected abstract void onSuccess(); /* was: success() */
	
	/**
	 * Marks the transaction as failed. 
	 */
	public abstract void fail();
	
	/**
	 * Finish the transaction. If fail() has been called during this transaction a rollback will be performed. 
	 * Otherwise the transaction will be committed.
	 */
	public final void finish() {
		onFinish();
		tx.onTransactionFinish(this, success);
	}

	protected abstract void onFinish(); /* was: finish() */
	
	/**
	 * Commit the transaction.
	 */
	public abstract void commit();
	
	/**
	 * Roles the transaction back .
	 */
	public abstract void rollback();
	
	/**
	 * flushes current state to database.
	 */
	public abstract void flush();

    /**
     * Check if the current transaction is active.
     * @return true if there is an active transaction
     */
    public abstract boolean isActive();
}
