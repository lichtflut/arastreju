/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import java.util.Set;

import org.arastreju.sge.TypeSystem;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNClass;

/**
 * <p>
 *  Neo specific implementation of TypeSystem.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoTypeSystem implements TypeSystem {
	
	private final ResourceResolver store;

	// -----------------------------------------------------
	
	/**
	 * @param neo4jDataStore
	 */
	public NeoTypeSystem(final ResourceResolver store) {
		this.store = store;
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.TypeSystem#getAllClasses()
	 */
	public Set<SNClass> getAllClasses() {
		ResourceNode clazz = store.resolve(RDFS.CLASS);
		return null;
	}

}
