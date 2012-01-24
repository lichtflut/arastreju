/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.structure;

import java.util.Comparator;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  Compares two resource nodes by it's serial number association.
 * </p>
 *
 * <p>
 * 	Created Jan 24, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class OrderBySerialNumber implements Comparator<ResourceNode> {

	/** 
	 * {@inheritDoc}
	 */
	public int compare(ResourceNode a, ResourceNode b) {
		SemanticNode serialA = SNOPS.singleObject(a, Aras.HAS_SERIAL_NUMBER);
		SemanticNode serialB = SNOPS.singleObject(b, Aras.HAS_SERIAL_NUMBER);
		if (serialA != null && serialA.isValueNode()) {
			if (serialB != null && serialB.isValueNode()) {
				return serialA.asValue().compareTo(serialB.asValue());
			} else {
				// only b has no serial number
				return -1;
			}
		} else {
			if (serialB != null && serialB.isValueNode()) {
				// only a has no serial number
				return 1;
			} else {
				// no serial number
				return 0;
			}
		}
	}

}
