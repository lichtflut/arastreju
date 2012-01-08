/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.model.associations;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.Statement;

/**
 * <p>
 * 	AssociationKeeper for a detached Resource.
 * </p>
 * 
 * <p>
 * 	Created: 26.01.2008
 * </p> 
 *
 * @author Oliver Tigges
 */
public class DetachedAssociationKeeper extends AbstractAssociationKeeper implements Serializable {
	
	private final Set<Statement> removedAssociations = new HashSet<Statement>();
	
	// -----------------------------------------------------

	/**
	 * Default constructor.
	 */
	public DetachedAssociationKeeper(){
		super();
		markResolved();
	}
	
	public DetachedAssociationKeeper(final Set<Statement> associations){
		super(associations);
		markResolved();
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAssociation(Statement assoc) {
		removedAssociations.add(assoc);
		return super.removeAssociation(assoc);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAttached() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Statement> getAssociationsForRemoval() {
		return removedAssociations;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void resolveAssociations() {
		throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE, 
				"DetachedAssociationKeeper cannot resolve Associations: " + this);
	}
	
	// ----------------------------------------------------
	
	@Override
	public String toString() {
		return super.toString();
	}

}
