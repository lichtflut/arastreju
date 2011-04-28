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
package org.arastreju.sge.apriori;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;

/**
 * <p>
 * 	A priori known Contexts.
 * </p>
 * 
 * <p>
 * 	Created: 28.04.2011
 * </p>
 *
 * @author Oliver Tigges 
 */
public interface CTX {
	
	// -- TYPES -------------------------------------------
	
	public static final String NAMESPACE_URI = "http://arastreju.org/contexts#";
	
	public static final ResourceID IDENT = new SimpleResourceID(NAMESPACE_URI, "IdentityManagement");
	public static final ResourceID TYPES = new SimpleResourceID(NAMESPACE_URI, "TypeSystem");
	
}
