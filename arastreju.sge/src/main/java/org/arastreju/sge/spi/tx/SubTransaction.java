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
package org.arastreju.sge.spi.tx;


import org.arastreju.sge.spi.WorkingContext;

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
public class SubTransaction implements BoundTransactionControl {

	private final BoundTransactionControl superTx;
	
	// -----------------------------------------------------

	/**
	 * @param superTx The super transaction.
	 */
	public SubTransaction(final BoundTransactionControl superTx) {
		this.superTx = superTx;
	}
	
	// -----------------------------------------------------
	
	@Override
	public void success() {
		superTx.success();
	}

    @Override
	public void commit() {
		// do nothing
	}

    @Override
	public void finish() {
		// do nothing
	}

    @Override
	public void fail() {
		superTx.fail();
	}

    @Override
	public void rollback() {
		superTx.rollback();
	}

    @Override
	public void flush() {
		superTx.flush();
	}

    @Override
    public boolean isActive() {
        return superTx.isActive();
    }

    // -- BoundTransactionControl -------------------------

    @Override
    public BoundTransactionControl bind(WorkingContext ctx) {
        if (!ctx.equals(getContext())) {
            throw new UnsupportedOperationException("The context may not be changed for sub transactions.");
        }
        return this;
    }

    @Override
    public WorkingContext getContext() {
        return superTx.getContext();
    }
}
