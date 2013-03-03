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
package org.arastreju.sge.apriori;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;

/**
 * <p>
 *   A priori known resource from Owl.
 * </p>
 * 
 * <p>
 * 	Created 07.01.2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class Owl {
	
	public static final String NAMESPACE_URI = "http://www.w3.org/2002/07/owl#";
	
	public static final ResourceID CLASS = new SimpleResourceID(NAMESPACE_URI, "Class");
	public static final ResourceID EQUIVALENT_CLASS = new SimpleResourceID(NAMESPACE_URI, "equivalentClass");
	public static final ResourceID EQUIVALENT_PROPERTY = new SimpleResourceID(NAMESPACE_URI, "equivalentProperty");
	public static final ResourceID SAME_AS = new SimpleResourceID(NAMESPACE_URI, "sameAs");
	public static final ResourceID INVERSE_OF = new SimpleResourceID(NAMESPACE_URI, "inverseOf");
	public static final ResourceID TRANSITIVE_PROPERTY = new SimpleResourceID(NAMESPACE_URI, "TransitiveProperty");
	public static final ResourceID SYMMETRIC_PROPERTY = new SimpleResourceID(NAMESPACE_URI, "SymmetricProperty");
	public static final ResourceID FUNCTIONAL_PROPERTY = new SimpleResourceID(NAMESPACE_URI, "FunctionalProperty");
	public static final ResourceID INVERSE_FUNCTIONAL_PROPERTY = new SimpleResourceID(NAMESPACE_URI, "InverseFunctionalProperty");
	public static final ResourceID OBJECT_PROPERTY = new SimpleResourceID(NAMESPACE_URI, "ObjectProperty");
	public static final ResourceID DATATYPE_PROPERTY = new SimpleResourceID(NAMESPACE_URI, "DatatypeProperty");

}
