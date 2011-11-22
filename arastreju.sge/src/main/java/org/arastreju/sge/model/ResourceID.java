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
package org.arastreju.sge.model;

import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 * 	General full qualified identifier of a resource.
 * </p>
 * 
 * <p>
 * 	Created: 23.04.2009
 * </p>
 * 
 * @author Oliver Tigges 
 * 
 */
public interface ResourceID extends SemanticNode {

	/**
	 * Returns the QualifiedName of the referenced Resource.
	 * @return The {@link QualifiedName}.
	 */
	QualifiedName getQualifiedName();
	
	/**
	 * Checks whether this resource references the same resource as the given.
	 * @param ref The reference to compare.
	 * @return true if this.getUri().equals(ref.getUri())
	 */
	boolean references(ResourceID ref);
	
}
