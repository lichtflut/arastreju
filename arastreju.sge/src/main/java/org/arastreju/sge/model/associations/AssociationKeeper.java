/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.model.associations;

import java.util.Set;

import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 * 	Generalized interface for resolving of a {@link SemanticNode}'s associations.
 * </p>
 *
 * <p>
 * 	Created Oct 11, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface AssociationKeeper {
	
	boolean isAttached();

	void reset();
	
	void add(Association assoc);

	boolean revoke(Association assoc);

	void remove(final Association assoc);

	Set<Association> getAssociations();

}