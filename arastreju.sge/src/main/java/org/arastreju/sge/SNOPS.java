/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  Utility class with static mehtods for Semantic Network OPerationS.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class SNOPS {

	public static String uri(final ResourceID rid){
		return rid.getQualifiedName().toURI();
	}
	
	public static String string(final SemanticNode node) {
		if (node == null) {
			return null;
		} else if (node.isValueNode()) {
			return node.asValue().getStringValue();
		} else {
			return node.asResource().getQualifiedName().toString();
		}
	}
	
}
