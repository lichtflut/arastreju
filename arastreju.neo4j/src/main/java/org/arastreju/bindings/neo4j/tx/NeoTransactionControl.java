/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.tx;

import org.arastreju.sge.persistence.TransactionControl;

/**
 * <p>
 *  Neo specific implementation of {@link TransactionControl}.
 *  
 *  Not thread safe!
 * </p>
 *
 * <p>
 * 	Created Jun 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoTransactionControl implements TransactionControl {
	
	private final TxProvider provider;
	
	private TxWrapper tx;
	
	// -----------------------------------------------------
	

	/**
	 * Constructor.
	 * @param provider The provider for transactions.
	 */
	public NeoTransactionControl(final TxProvider provider) {
		this.provider = provider;
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.persistence.TransactionControl#begin()
	 */
	public void begin() {
		if (provider.inTransaction()) {
			throw new IllegalStateException("Transaction already open.");
		}
		tx = provider.begin();
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.persistence.TransactionControl#beginOrContinue()
	 */
	public void beginOrContinue() {
		if (!provider.inTransaction()) {
			tx = provider.begin();
		}
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.persistence.TransactionControl#commit()
	 */
	public void commit() {
		assertTxStarted();
		tx.markSuccessful();
		tx.finish();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.persistence.TransactionControl#rollback()
	 */
	public void rollback() {
		assertTxStarted();
		tx.markFailure();
		tx.finish();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.persistence.TransactionControl#isInTansaction()
	 */
	public boolean isInTansaction() {
		return provider.inTransaction();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.persistence.TransactionControl#flush()
	 */
	public void flush() {
	}
	
	// -----------------------------------------------------
	
	protected void assertTxStarted() {
		if (tx == null) {
			throw new RuntimeException("No transaction open.");
		}
	}

}
