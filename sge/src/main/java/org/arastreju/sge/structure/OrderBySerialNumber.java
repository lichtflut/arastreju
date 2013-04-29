/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.structure;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

import java.util.Comparator;

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

    @Override
	public int compare(ResourceNode a, ResourceNode b) {
		SemanticNode serialA = SNOPS.fetchObject(a, Aras.HAS_SERIAL_NUMBER);
		SemanticNode serialB = SNOPS.fetchObject(b, Aras.HAS_SERIAL_NUMBER);
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
