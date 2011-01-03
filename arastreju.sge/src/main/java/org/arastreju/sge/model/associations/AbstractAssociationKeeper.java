/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.model.associations;

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
		if (assoc.isPersistent()){
			revokedAssociations.add(assoc);
		}
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
	

	protected abstract void resolveAssociations();
	
	protected void markResolved(){
		resolved = true;
	}

}