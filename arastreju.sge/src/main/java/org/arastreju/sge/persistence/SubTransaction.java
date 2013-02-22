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
