/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import java.util.Collection;

import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.sge.Organizer;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Neo implementation of Organizer.
 * </p>
 *
 * <p>
 * 	Created Sep 22, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoOrganizer implements Organizer {

	@SuppressWarnings("unused")
	private final ResourceIndex index;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param index The index.
	 */
	public NeoOrganizer(final ResourceIndex index) {
		this.index = index;
	}
	
	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public Collection<Namespace> getNamespaces() {
		return null;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public Namespace registerNamespace(final String namespace, final String defaultPrefix) {
		return null;
	}
	
	// -----------------------------------------------------

	/** 
	 * {@inheritDoc}
	 */
	public Collection<Context> getContexts() {
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	public Context registerContext(QualifiedName qn) {
		return null;
	}
	
}
