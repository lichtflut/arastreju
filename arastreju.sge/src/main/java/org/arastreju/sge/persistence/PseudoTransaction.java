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
 * 	This pseudo implementation does not really work transactional.
 * </p>
 * 
 * <p>
 * 	Created: 20 Jul, 2012
 * </p>
 *
 * @author Raphael Esterle
 */
public class PseudoTransaction implements TransactionControl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void success() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finish() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive() {
		return true;
	}

}
