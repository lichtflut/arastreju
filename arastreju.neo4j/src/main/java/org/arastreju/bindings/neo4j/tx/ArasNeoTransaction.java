/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.tx;

import org.arastreju.sge.persistence.TransactionControl;
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
class ArasNeoTransaction implements TransactionControl {

	private Transaction tx;
	private boolean active; 
	
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
	
	/** 
	 * {@inheritDoc}
	 */
	public void success() {
		tx.success();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void fail() {
		tx.failure();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void finish() {
		tx.finish();
		tx = null;
		active = false;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void commit() {
		success();
		finish();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void rollback() {
		fail();
		finish();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void flush() {
	}
	
	// -----------------------------------------------------

}
