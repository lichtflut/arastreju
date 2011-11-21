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
package org.arastreju.sge.persistence;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Resolver for ResourceIDs.
 * </p>
 *
 * <p>
 * 	Created Nov 21, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ResourceResolver {

	/**
	 * Resolve a resource by it's ID. If it does not exist, it will be created.
	 * @param id The ID.
	 * @return The resource node.
	 */
	ResourceNode resolve(ResourceID id);

}