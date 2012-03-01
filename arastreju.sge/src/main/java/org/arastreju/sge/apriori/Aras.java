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

import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
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
	
	String CTX_NAMESPACE_URI = "http://arastreju.org/contexts#";
	
	// -- CONTEXTS ----------------------------------------
	
	public static final Context IDENT = new SimpleContextID(CTX_NAMESPACE_URI, "IdentityManagement");
	public static final Context TYPES = new SimpleContextID(CTX_NAMESPACE_URI, "TypeSystem");
	
	
	// -- TYPES -------------------------------------------
	
	ResourceID CONTEXT = new SimpleResourceID(NAMESPACE_URI, "Context");
	ResourceID NAMESPACE = new SimpleResourceID(NAMESPACE_URI, "Namespace");
	ResourceID DIAGRAM = new SimpleResourceID(NAMESPACE_URI, "Diagram");
	ResourceID REGISTER = new SimpleResourceID(NAMESPACE_URI, "Register");
	
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
	
	// -- MODEL SELECTIONS --------------------------------
	
	ResourceID CONTAINS = new SimpleResourceID(NAMESPACE_URI, "contains");
	ResourceID SELECTS_BY_QUERY = new SimpleResourceID(NAMESPACE_URI, "selectsByQuery");
	ResourceID SELECTS_BY_NAMESPACE = new SimpleResourceID(NAMESPACE_URI, "selectsByNamespace");
	ResourceID HAS_DEFAULT_NAMESPACE = new SimpleResourceID(NAMESPACE_URI, "hasDefaultNamespace");
	ResourceID IS_IN_CONTEXT = new SimpleResourceID(NAMESPACE_URI, "isInContext");
	
	// -- IDENTITY MANAGEMENT -----------------------------
	
	ResourceID IDENTITY = new SimpleResourceID(NAMESPACE_URI, "Identity");
	ResourceID USER = new SimpleResourceID(NAMESPACE_URI, "User");
	ResourceID GROUP = new SimpleResourceID(NAMESPACE_URI, "Group");
	ResourceID CREDENTIAL = new SimpleResourceID(NAMESPACE_URI, "Credential");
	ResourceID ROLE = new SimpleResourceID(NAMESPACE_URI, "Role");
	ResourceID PERMISSION = new SimpleResourceID(NAMESPACE_URI, "Permission");
	ResourceID DOMAIN = new SimpleResourceID(NAMESPACE_URI, "Domain");
	
	ResourceID IDENTIFIED_BY = new SimpleResourceID(NAMESPACE_URI, "isIdentifiedBy");
	ResourceID HAS_CREDENTIAL = new SimpleResourceID(NAMESPACE_URI, "hasCredential");
	ResourceID HAS_UNIQUE_NAME = new SimpleResourceID(NAMESPACE_URI, "hasUniqueName");
	ResourceID HAS_TITLE = new SimpleResourceID(NAMESPACE_URI, "hasTitle");
	ResourceID HAS_DESCRIPTION = new SimpleResourceID(NAMESPACE_URI, "hasDescription");
	ResourceID HAS_ROLE = new SimpleResourceID(NAMESPACE_URI, "hasRole");
	ResourceID HAS_PERMISSION = new SimpleResourceID(NAMESPACE_URI, "hasPermission");
	
	ResourceID BELONGS_TO_DOMAIN = new SimpleResourceID(NAMESPACE_URI, "belongsToDomain");
	ResourceID IS_DOMESTIC_DOMAIN = new SimpleResourceID(NAMESPACE_URI, "isDomesticDomain");
	
	// -- SCHEMAS -----------------------------------------
	
	ResourceID ACTIVITY_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "ActivitySchema");
	ResourceID ACTELLON_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "ActellonSchema");
	ResourceID RELATION_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "RelationSchema");
	
	ResourceID HAS_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "hasSchema");
	ResourceID HAS_STATEMENT_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "hasStatementSchema");
	ResourceID HAS_ACTELLON_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "hasActellonSchema");
	ResourceID HAS_ATTRIBUTE_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "hasAttributeSchema");
	ResourceID HAS_RELATION_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "hasRelationSchema");
	ResourceID SUB_SCHEMA_OF = new SimpleResourceID(NAMESPACE_URI, "subSchemaOf");
	ResourceID CONTAINS_CHOICE_ELEMENTS = new SimpleResourceID(NAMESPACE_URI, "containsChoiceElements");
	
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
