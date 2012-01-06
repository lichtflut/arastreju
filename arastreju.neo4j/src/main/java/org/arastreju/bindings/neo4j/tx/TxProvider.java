/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.tx;

import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.persistence.TxAction;
import org.arastreju.sge.persistence.TxResultAction;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * <p>
 *  Transaction provider based on neo4j {@link GraphDatabaseService} transactions.
 * </p>
 *
 * <p>
 * 	Created Jun 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class TxProvider {
	
	private final GraphDatabaseService gdbService;
	
	private ArasNeoTransaction tx;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param gdbService The service for this TX Control.
	 */
	public TxProvider(final GraphDatabaseService gdbService) {
		this.gdbService = gdbService;
	}
	
	// -----------------------------------------------------
	
	/**
	 * Begin a new transaction if not already one open.
	 * @return An active transaction.
	 */
	public TransactionControl begin() {
		if (!inTransaction()) {
			tx = new ArasNeoTransaction(gdbService.beginTx());
			return tx;
		} else {
			return new ArasNeoSubTransaction(tx);
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
	
}
