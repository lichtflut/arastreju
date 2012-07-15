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
package org.arastreju.sge.apriori;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;

/**
 * <p>
 * 	A priori known URIs for Arastreju.
 * </p>
 * 
 * <p>
 * 	Created: 09.11.2009
 * </p>
 *
 * @author Oliver Tigges 
 */
public interface Aras {
	
	String NAMESPACE_URI = "http://arastreju.org/kernel#";
	
	// -- TYPES -------------------------------------------
	
	ResourceID CONTEXT = new SimpleResourceID(NAMESPACE_URI, "Context");
	ResourceID NAMESPACE = new SimpleResourceID(NAMESPACE_URI, "Namespace");

	// -- DATATYPES ---------------------------------------
	
	ResourceID DATATYPE_STRING = new SimpleResourceID("http://arastreju.org/kernel#", "String");
	ResourceID DATATYPE_BOOLEAN = new SimpleResourceID("http://arastreju.org/kernel#", "Boolean");
	ResourceID DATATYPE_INTEGER = new SimpleResourceID("http://arastreju.org/kernel#", "Integer");
	ResourceID DATATYPE_DECIMAL = new SimpleResourceID("http://arastreju.org/kernel#", "Decimal");;
	ResourceID DATATYPE_TIMESTAMP = new SimpleResourceID("http://arastreju.org/kernel#", "Timestamp");
	ResourceID DATATYPE_DATE = new SimpleResourceID("http://arastreju.org/kernel#", "Date");
	ResourceID DATATYPE_TIME_OF_DAY = new SimpleResourceID("http://arastreju.org/kernel#", "TimeOfDay");
	ResourceID DATATYPE_URI = new SimpleResourceID("http://www.w3.org/2000/01/rdf-schema#", "URI");
	ResourceID DATATYPE_PROPER_NAME = new SimpleResourceID("http://arastreju.org/kernel#", "ProperName");
	ResourceID DATATYPE_TERM = new SimpleResourceID("http://arastreju.org/kernel#", "Term");
	ResourceID DATATYPE_RESOURCE = new SimpleResourceID("http://www.w3.org/2000/01/rdf-schema#", "Resource");
	
	// -- VALUES ---------------------------------------
	
	ResourceID TRUE = new SimpleResourceID("http://arastreju.org/kernel#", "True");
	ResourceID FALSE = new SimpleResourceID("http://arastreju.org/kernel#", "False");
	
	// -- ORGANIZATION --------------------------------
	
	ResourceID HAS_NAME = new SimpleResourceID(NAMESPACE_URI, "hasName");
	ResourceID HAS_PREFIX = new SimpleResourceID(NAMESPACE_URI, "hasPrefix");
	ResourceID HAS_URI = new SimpleResourceID(NAMESPACE_URI, "hasURI");

	// -- CONSTRAINTS -------------------------------------
	
	ResourceID HAS_PROPER_NAME = new SimpleResourceID(NAMESPACE_URI, "hasProperName");
	ResourceID HAS_FORENAME = new SimpleResourceID(NAMESPACE_URI, "hasForename");
	ResourceID HAS_SURNAME = new SimpleResourceID(NAMESPACE_URI, "hasSurname");
	ResourceID HAS_MIDDLE_NAME = new SimpleResourceID(NAMESPACE_URI, "hasMiddleName");
	ResourceID HAS_NAME_PART = new SimpleResourceID(NAMESPACE_URI, "hasNamePart");
	ResourceID HAS_BRAND_NAME = new SimpleResourceID(NAMESPACE_URI, "hasBrandName");
	
	// -- DATA_STRUCTURE ----------------------------------
	
	ResourceID IS_PREDECESSOR_OF = new SimpleResourceID(NAMESPACE_URI, "isPredecessorOf");
	ResourceID IS_SUCCESSOR_OF = new SimpleResourceID(NAMESPACE_URI, "isSuccessorOf");
	
	ResourceID HAS_SERIAL_NUMBER = new SimpleResourceID(NAMESPACE_URI, "hasSerialNumber");

	ResourceID IS_NATURALLY_ORDERED_BY = new SimpleResourceID(NAMESPACE_URI, "isNaturallyOrderedBy");

	// -- INFERENCES --------------------------------------
	
	ResourceID INVERSE_OF = new SimpleResourceID(NAMESPACE_URI, "inverseOf");

}
