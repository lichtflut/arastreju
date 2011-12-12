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
 * URIs known a priori.
 * 
 * Created: 23.10.2008
 *
 * @author Oliver Tigges
 */
public final class RDF {
	
	public static final String NAMESPACE_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
	public static final ResourceID TYPE = new SimpleResourceID(NAMESPACE_URI, "type");
	public static final ResourceID SUBJECT = new SimpleResourceID(NAMESPACE_URI, "subject");
	public static final ResourceID PREDICATE = new SimpleResourceID(NAMESPACE_URI, "predicate");
	public static final ResourceID OBJECT = new SimpleResourceID(NAMESPACE_URI, "object");
	public static final ResourceID PROPERTY = new SimpleResourceID(NAMESPACE_URI, "Property");
	
}
