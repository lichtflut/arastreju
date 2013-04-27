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
package org.arastreju.sge.persistence;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Resolver for resources.
 * <p/>
 *
 * <p>
 *  Created Jul 2, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ResourceResolver {

    /**
     * Resolve a resource identifier.
     * @param rid The resource identifier.
     * @return The existing or newly created ResourceNode for this identifier.
     */
    ResourceNode resolve(ResourceID rid);

    /**
     * Find a resource by it's qualified name.
     * @param qn The qualified name.
     * @return The resource node or null.
     */
    ResourceNode findResource(QualifiedName qn);

}
