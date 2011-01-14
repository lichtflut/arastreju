/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.bindings.neo4j.impl;

import java.util.HashMap;
import java.util.Map;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Registry for Arastreju resources.
 * </p>
 *
 * <p>
 * 	Created Sep 9, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class ResourceRegistry {
	
	private final Map<QualifiedName, ResourceNode> resourceMap = new HashMap<QualifiedName, ResourceNode>();
	
	// -----------------------------------------------------
	
	public boolean contains(final QualifiedName qn){
		return resourceMap.containsKey(qn);
	}
	
	public void register(final ResourceNode resource){
		resourceMap.put(resource.getQualifiedName(), resource);
	}
	
	public void unregister(final ResourceNode resource){
		resourceMap.remove(resource.getQualifiedName());
	}
	
	public ResourceNode get(final QualifiedName qn){
		return resourceMap.get(qn);
	}

	// -----------------------------------------------------
	
	public void clear(){
		resourceMap.clear();
	}
}
