/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * The Arastreju-Neo4j binding is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arastreju.sge.persistence;

import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.repl.ArasLiveReplicator;
import org.arastreju.sge.repl.NoOpReplicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final Logger logger = LoggerFactory.getLogger(TxProvider.class);
	private TransactionControl tx;
	private ArasLiveReplicator repl;

	// -----------------------------------------------------

	/**
	 * Constructor.
	 */
	public TxProvider() {
	}
	
	public void initRepl(ArastrejuProfile profile) {
		this.repl = createReplicator();
		String rcvAddr = profile.getProperty(ArastrejuProfile.REPLICATOR_RECEIVER_ADDR);
		String dspHost = profile.getProperty(ArastrejuProfile.REPLICATOR_DISPATCHER_HOST);
		int rcvPort = Integer.parseInt(profile.getProperty(ArastrejuProfile.REPLICATOR_RECEIVER_PORT));
		int dspPort = Integer.parseInt(profile.getProperty(ArastrejuProfile.REPLICATOR_DISPATCHER_PORT));
		repl.init(rcvAddr, rcvPort, dspHost, dspPort);
	}
	
	// -----------------------------------------------------
	
	/**
	 * Begin a new transaction if not already one open.
	 * @return An active transaction.
	 */
	public TransactionControl begin() {
		if (!inTransaction()) {
			tx = newTx();
			repl.reset();
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

	/**
	 * @return reference to the replicator (source part)
	 */
	public ArasLiveReplicator getRepl() {
		return repl;
	}
	
	/** 
	 * called by finished transactions. If successful, the replicator
	 * is triggered to send out the data it acquired in the course
	 * of this transaction
	 */
	public void onTransactionFinish(TransactionControl tx, boolean success) {
		if (this.tx == tx) { //root- or sub-transaction?
			if (success) //root-transaction succeeded
				repl.dispatch();
			
			repl.reset();
		}
	}
	
    // ----------------------------------------------------

    protected abstract TransactionControl newTx();

    protected TransactionControl newSubTx(TransactionControl parent) {
    	return new SubTransaction(parent);
    }
    
    /**
     * subclasses should override this in case they want replication
     * @return a newly created backend-specific replicator, or null for no replication facilities
     */
    protected ArasLiveReplicator createReplicator() {
    	return new NoOpReplicator();
    }
	
}
