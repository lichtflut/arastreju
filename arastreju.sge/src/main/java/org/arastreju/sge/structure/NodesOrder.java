/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.structure;

import java.util.Collections;
import java.util.List;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.ValueNode;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jan 24, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class NodesOrder {
	
	public static void sortResources(List<ResourceNode> nodes) {
		
	}
	
	public static void sortValues(List<ValueNode> nodes) {
		Collections.sort(nodes);
	}
	
	// ----------------------------------------------------
	
	public static void sortBySerialNumber(List<ResourceNode> nodes) {
		Collections.sort(nodes, new OrderBySerialNumber());
	}

}
