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
public class SubTransaction implements TransactionControl {

	private final TransactionControl superTx;
	
	// -----------------------------------------------------

	/**
	 * @param superTx The super transaction.
	 */
	public SubTransaction(final TransactionControl superTx) {
		this.superTx = superTx;
	}
	
	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public void success() {
		superTx.success();
	}
	
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        return superTx.isActive();
    }
}
