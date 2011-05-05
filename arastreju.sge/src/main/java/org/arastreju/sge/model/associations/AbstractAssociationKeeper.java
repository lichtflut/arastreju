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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

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
	private final Set<Association> revokedAssociations = new HashSet<Association>();
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

	public void reset() {
		associations.clear();
		revokedAssociations.clear();
		resolved = false;
	}
	
	public void add(final Association assoc) {
		associations.add(assoc);
	}

	public boolean revoke(final Association assoc) {
		revokedAssociations.add(assoc);
		return getAssociations().remove(assoc);
	}

	public void remove(final Association assoc) {
		getAssociations().remove(assoc);
	}

	public synchronized Set<Association> getAssociations() {
		if (!resolved){
			if (!associations.isEmpty()){
				throw new IllegalArgumentException("node has already an association attached: " + associations);
			}
			resolved = true;
			resolveAssociations();
		}
		return Collections.unmodifiableSet(associations);
	}
	
	/**
	 * @return the revokedAssociations
	 */
	public Set<Association> getRevokedAssociations() {
		return revokedAssociations;
	}
	
	// -----------------------------------------------------

	/**
	 * To be overridden by sub classes.
	 */
	protected abstract void resolveAssociations();
	
	// -----------------------------------------------------
	
	protected void markResolved(){
		resolved = true;
	}
	
	/**
	 * Creates a new Association object for an existing association that has been resolved.
	 */
	protected void addResolvedAssociation(final ResourceNode subject, final ResourceID predicate, final SemanticNode object, final Context... contexts) {
		associations.add(new Association(subject, predicate, object, contexts));
	}

}