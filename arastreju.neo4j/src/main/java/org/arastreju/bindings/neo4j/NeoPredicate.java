/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.RelationshipType;

/**
 * [Description]
 *
 * @author Oliver Tigges
 */
public class NeoPredicate implements RelationshipType {
	
	public final QualifiedName qn;
	
	public NeoPredicate(final QualifiedName qn) {
		this.qn = qn;
	}

	/* (non-Javadoc)
	 * @see org.neo4j.graphdb.RelationshipType#name()
	 */
	public String name() {
		return qn.toURI();
	}

}
