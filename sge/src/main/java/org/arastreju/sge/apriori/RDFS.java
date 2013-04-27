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
 *  A priori known resource from RDFS.
 * </p>
 *
 * <p>
 * 	Created Jan 4, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class RDFS {
	
	public static final String NAMESPACE_URI = "http://www.w3.org/2000/01/rdf-schema#";
	
	public static final ResourceID CLASS = new SimpleResourceID(NAMESPACE_URI, "Class");
	public static final ResourceID SUB_CLASS_OF = new SimpleResourceID(NAMESPACE_URI, "subClassOf");
	public static final ResourceID SUB_PROPERTY_OF = new SimpleResourceID(NAMESPACE_URI, "subPropertyOf");
	public static final ResourceID DATATYPE = new SimpleResourceID(NAMESPACE_URI, "Datatype");
	public static final ResourceID DOMAIN = new SimpleResourceID(NAMESPACE_URI, "domain");
	public static final ResourceID RANGE = new SimpleResourceID(NAMESPACE_URI, "range");
	public static final ResourceID LABEL = new SimpleResourceID(NAMESPACE_URI, "label");
	public static final ResourceID COMMENT = new SimpleResourceID(NAMESPACE_URI, "comment");

}
