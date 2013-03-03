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
package org.arastreju.sge.model.associations;

import org.arastreju.sge.model.Statement;

import java.io.Serializable;
import java.util.Collections;
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
public abstract class AbstractAssociationKeeper implements AssociationKeeper, Serializable {

	private Set<Statement> associations;
	
	private Set<Statement> removedAssociations;
	
	private boolean resolved;
	
	// -----------------------------------------------------

	/**
	 * Creates a new instance.
	 */
	public AbstractAssociationKeeper() {
	}
	
	/**
	 * Creates a new instance.
	 * @param associations The associations to be kept.
	 */
	protected AbstractAssociationKeeper(final Set<Statement> associations) {
		getAssociationsDirectly().addAll(associations);
		this.resolved = true;
	}
	
	// -----------------------------------------------------
	
	@Override
	public Set<Statement> getAssociations() {
		if (!resolved){
			if (associations != null && !associations.isEmpty()){
				throw new IllegalArgumentException("node has already an association attached: " + associations);
			}
			resolved = true;
			resolveAssociations();
		}
		return getAssociationsDirectly();
	}

    @Override
	public void addAssociation(final Statement assoc) {
		getAssociations().add(assoc);
	}
	
	// ----------------------------------------------------

    @Override
	public boolean removeAssociation(final Statement assoc) {
		if (removedAssociations == null) {
			removedAssociations = new HashSet<Statement>();
		}
		removedAssociations.add(assoc);
		return getAssociations().remove(assoc);
	}

    @Override
	public Set<Statement> getAssociationsForRemoval() {
		if (removedAssociations == null) {
			return Collections.emptySet();
		}
		return removedAssociations;
	}
	
	// -----------------------------------------------------
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		if (resolved) {
			sb.append(" resolved ");
		}
		sb.append("\n");
		if (associations != null) {
			for (Statement stmt : associations) {
				sb.append("\t" + stmt + "\n");	
			}
		}
		return sb.toString();
	}
	
	// -----------------------------------------------------

	/**
	 * To be overridden by sub classes.
	 */
	protected abstract void resolveAssociations();

	// -----------------------------------------------------
	
	protected Set<Statement> getAssociationsDirectly() {
		if (associations == null) {
			associations = new HashSet<Statement>();
		}
		return associations;
	}
	
	protected AbstractAssociationKeeper markResolved(){
		resolved = true;
		return this;
	}

    /**
     * Reset the state of this keeper. Will be marked as not resolved and loose all associations.
     */
    protected void reset() {
        resolved = false;
        associations = null;
    }


}