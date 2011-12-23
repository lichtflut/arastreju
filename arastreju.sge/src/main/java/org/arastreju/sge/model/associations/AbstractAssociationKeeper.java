/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  Abstract base for an association keeper.
 * </p>
 *
 * <p>
 * 	Created Oct 11, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractAssociationKeeper implements AssociationKeeper {

	private final Set<Association> associations = new HashSet<Association>();
	
	private boolean resolved;
	
	// -----------------------------------------------------

	/**
	 * Creates a new instance.
	 */
	public AbstractAssociationKeeper() {
		super();
	}
	
	/**
	 * Creates a new instance.
	 * @param associations The associations to be kept.
	 */
	public AbstractAssociationKeeper(final Set<Association> associations) {
		this.associations.addAll(associations);
		this.resolved = true;
	}
	
	// -----------------------------------------------------
	
	public synchronized Set<Association> getAssociations() {
		if (!resolved){
			if (!associations.isEmpty()){
				throw new IllegalArgumentException("node has already an association attached: " + associations);
			}
			resolved = true;
			resolveAssociations();
		}
		return associations;
	}

	public void add(final Association assoc) {
		getAssociations().add(assoc);
	}

	public boolean remove(final Association assoc) {
		return getAssociations().remove(assoc);
	}

	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		if (resolved) {
			sb.append(" resolved ");
		}
		sb.append(associations);
		return sb.toString();
	}
	
	// -----------------------------------------------------

	/**
	 * To be overridden by sub classes.
	 */
	protected abstract void resolveAssociations();

	// -----------------------------------------------------
	
	protected Set<Association> getAssociationsDirectly() {
		return associations;
	}
	
	protected AbstractAssociationKeeper markResolved(){
		resolved = true;
		return this;
	}
	
}