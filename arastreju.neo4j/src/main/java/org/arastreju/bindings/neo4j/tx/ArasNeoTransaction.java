/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.tx;

import org.neo4j.graphdb.Transaction;

/**
 * <p>
 *  Direct wrapper of a Neo4j Transaction.
 * </p>
 *
 * <p>
 * 	Created Jun 7, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
class ArasNeoTransaction implements TxWrapper {

	private Transaction tx;
	private boolean active; 
	private boolean failure;
	
	// -----------------------------------------------------

	/**
	 * Constructor.
	 * @param tx The transaction.
	 */
	public ArasNeoTransaction(final Transaction tx) {
		this.tx = tx;
		this.active = true;
	}
	
	// -----------------------------------------------------
	
	/**
	 * @return true if the transaction is active.
	 */
	public boolean isActive() {
		return active;
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.bindings.neo4j.tx.TxWrapper#markSuccessful()
	 */
	public void markSuccessful() {
		tx.success();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.bindings.neo4j.tx.TxWrapper#markFailure()
	 */
	public void markFailure() {
		tx.failure();
		failure = true;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.bindings.neo4j.tx.TxWrapper#finish()
	 */
	public void finish() {
		tx.finish();
		tx = null;
		active = false;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.bindings.neo4j.tx.TxWrapper#isFailure()
	 */
	public boolean isFailure() {
		return failure;
	}
	
}
