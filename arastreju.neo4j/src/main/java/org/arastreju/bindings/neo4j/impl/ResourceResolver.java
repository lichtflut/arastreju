/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Node;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ResourceResolver {

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#findResource(com.sun.xml.internal.fastinfoset.QualifiedName)
	 */
	ResourceNode findResource(final QualifiedName qn);

	ResourceNode findResource(final Node neoNode);

	/* (non-Javadoc)
	 * @see org.arastreju.sge.ModellingConversation#resolve(org.arastreju.sge.model.ResourceID)
	 */
	ResourceNode resolve(final ResourceID resource);

}