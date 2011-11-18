/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.tx;

import org.arastreju.sge.persistence.TransactionControl;


/**
 * <p>
 *  Sub transaction.
 * </p>
 *
 * <p>
 * 	Created Jun 7, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
class ArasNeoSubTransaction implements TransactionControl {

	private final TransactionControl superTx;
	
	// -----------------------------------------------------

	/**
	 * @param superTx The super transaction.
	 */
	public ArasNeoSubTransaction(final TransactionControl superTx) {
		this.superTx = superTx;
	}
	
	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public void success() {
		superTx.success();
	};
	
	/** 
	 * {@inheritDoc}
	 */
	public void commit() {
		// do nothing
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void finish() {
		// do nothing
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void fail() {
		superTx.fail();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void rollback() {
		superTx.rollback();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void flush() {
		superTx.flush();
	}
	
}
