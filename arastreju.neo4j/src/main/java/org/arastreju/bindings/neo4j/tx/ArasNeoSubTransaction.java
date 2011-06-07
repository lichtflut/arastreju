/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.tx;


/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jun 7, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
class ArasNeoSubTransaction implements TxWrapper {

	private final TxWrapper superTx;
	
	// -----------------------------------------------------

	/**
	 * @param superTx The super transaction.
	 */
	public ArasNeoSubTransaction(final TxWrapper superTx) {
		this.superTx = superTx;
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.bindings.neo4j.tx.TxWrapper#markSuccessful()
	 */
	public void markSuccessful() {
		superTx.markSuccessful();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.bindings.neo4j.tx.TxWrapper#markFailure()
	 */
	public void markFailure() {
		superTx.markFailure();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.bindings.neo4j.tx.TxWrapper#finish()
	 */
	public void finish() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see org.arastreju.bindings.neo4j.tx.TxWrapper#isFailure()
	 */
	public boolean isFailure() {
		return superTx.isFailure();
	}

}
