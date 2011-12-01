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

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.ResourceResolver;
import org.neo4j.graphdb.Node;

/**
 * <p>
 *  Interface for resource resolving.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface NeoResourceResolver extends ResourceResolver {

	/**
	 * Find a resource by it's qualified name.
	 * @param qn The qualified name.
	 * @return The resource node or null.
	 */
	ResourceNode findResource(QualifiedName qn);

	/**
	 * Find a resource by it's corresponding neo node.
	 * @param neoNode The neo node.
	 * @return The resource node.
	 */
	ResourceNode resolve(Node neoNode);
	
}