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

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.ValueNode;

import java.util.Collections;
import java.util.List;

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
