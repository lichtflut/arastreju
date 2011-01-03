/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.extensions;

import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Sep 9, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class SNResourceNeo extends SNResource {

	/**
	 * @param name
	 * @param namespace
	 * @param associationKeeper
	 */
	public SNResourceNeo(final String name, final Namespace namespace, final NeoAssociationKeeper associationKeeper) {
		super(name, namespace, associationKeeper);
	}
	
	/**
	 * 
	 */
	public SNResourceNeo(final QualifiedName qn, final NeoAssociationKeeper associationKeeper) {
		super(qn, associationKeeper);
	}
	
}
