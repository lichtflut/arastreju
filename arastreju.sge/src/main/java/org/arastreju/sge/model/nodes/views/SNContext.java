/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.model.nodes.views;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 * 	Resource representing a context for semantic statements.
 * </p>
 * 
 * <p>
 * 	Created: 22.02.2010
 * </p>
 *
 * @author Oliver Tigges
 *
 */
public class SNContext extends ResourceView implements Context {

	/**
	 * Create a new context view for given resource.
	 * @param resource The context resource to be wrapped.
	 */
	public SNContext(final ResourceNode resource) {
		super(resource);
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public int compareTo(final Context other) {
		return getQualifiedName().compareTo(other.getQualifiedName());
	}
	
}
