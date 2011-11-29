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
	
	public static final String NAMESPACE_URI = "http://arastreju.org/kernel#";
	
	// -- TYPES -------------------------------------------
	
	public static final ResourceID CONTEXT = new SimpleResourceID(NAMESPACE_URI, "Context");
	public static final ResourceID NAMESPACE = new SimpleResourceID(NAMESPACE_URI, "Namespace");
	public static final ResourceID DIAGRAM = new SimpleResourceID(NAMESPACE_URI, "Diagram");
	public static final ResourceID REGISTER = new SimpleResourceID(NAMESPACE_URI, "Register");
	
	// -- DATATYPES ---------------------------------------
	
	public static final ResourceID DATATYPE_STRING = new SimpleResourceID("http://arastreju.org/kernel#", "String");
	public static final ResourceID DATATYPE_BOOLEAN = new SimpleResourceID("http://arastreju.org/kernel#", "Boolean");
	public static final ResourceID DATATYPE_INTEGER = new SimpleResourceID("http://arastreju.org/kernel#", "Integer");
	public static final ResourceID DATATYPE_DECIMAL = new SimpleResourceID("http://arastreju.org/kernel#", "Decimal");;
	public static final ResourceID DATATYPE_TIMESTAMP = new SimpleResourceID("http://arastreju.org/kernel#", "Timestamp");
	public static final ResourceID DATATYPE_DATE = new SimpleResourceID("http://arastreju.org/kernel#", "Date");
	public static final ResourceID DATATYPE_TIME_OF_DAY = new SimpleResourceID("http://arastreju.org/kernel#", "TimeOfDay");
	public static final ResourceID DATATYPE_URI = new SimpleResourceID("http://www.w3.org/2000/01/rdf-schema#", "URI");
	public static final ResourceID DATATYPE_PROPER_NAME = new SimpleResourceID("http://arastreju.org/kernel#", "ProperName");
	public static final ResourceID DATATYPE_TERM = new SimpleResourceID("http://arastreju.org/kernel#", "Term");
	public static final ResourceID DATATYPE_RESOURCE = new SimpleResourceID("http://www.w3.org/2000/01/rdf-schema#", "Resource");
	
	// -- VALUES ---------------------------------------
	
	public static final ResourceID TRUE = new SimpleResourceID("http://arastreju.org/kernel#", "True");
	public static final ResourceID FALSE = new SimpleResourceID("http://arastreju.org/kernel#", "False");
	
	// -- ORGANIZATION --------------------------------
	
	public static final ResourceID HAS_NAME = new SimpleResourceID(NAMESPACE_URI, "hasName");
	public static final ResourceID HAS_PREFIX = new SimpleResourceID(NAMESPACE_URI, "hasPrefix");
	public static final ResourceID HAS_URI = new SimpleResourceID(NAMESPACE_URI, "hasURI");
	
	// -- MODEL SELECTIONS --------------------------------
	
	public static final ResourceID CONTAINS = new SimpleResourceID(NAMESPACE_URI, "contains");
	public static final ResourceID SELECTS_BY_QUERY = new SimpleResourceID(NAMESPACE_URI, "selectsByQuery");
	public static final ResourceID SELECTS_BY_NAMESPACE = new SimpleResourceID(NAMESPACE_URI, "selectsByNamespace");
	public static final ResourceID HAS_DEFAULT_NAMESPACE = new SimpleResourceID(NAMESPACE_URI, "hasDefaultNamespace");
	public static final ResourceID IS_IN_CONTEXT = new SimpleResourceID(NAMESPACE_URI, "isInContext");
	
	// -- IDENTITY MANAGEMENT -----------------------------
	
	public static final ResourceID IDENTITY = new SimpleResourceID(NAMESPACE_URI, "Identity");
	public static final ResourceID USER = new SimpleResourceID(NAMESPACE_URI, "User");
	public static final ResourceID GROUP = new SimpleResourceID(NAMESPACE_URI, "Group");
	public static final ResourceID CREDENTIAL = new SimpleResourceID(NAMESPACE_URI, "Credential");
	public static final ResourceID ROLE = new SimpleResourceID(NAMESPACE_URI, "Role");
	public static final ResourceID PERMISSION = new SimpleResourceID(NAMESPACE_URI, "Permission");
	
	public static final ResourceID IDENTIFIED_BY = new SimpleResourceID(NAMESPACE_URI, "isIdentifiedBy");
	public static final ResourceID HAS_CREDENTIAL = new SimpleResourceID(NAMESPACE_URI, "hasCredential");
	public static final ResourceID HAS_EMAIL = new SimpleResourceID(NAMESPACE_URI, "hasEmail");
	public static final ResourceID HAS_UNIQUE_NAME = new SimpleResourceID(NAMESPACE_URI, "hasUniqueName");
	public static final ResourceID HAS_ROLE = new SimpleResourceID(NAMESPACE_URI, "hasRole");
	public static final ResourceID HAS_PERMISSION = new SimpleResourceID(NAMESPACE_URI, "hasPermission");
	
	// -- SCHEMAS -----------------------------------------
	
	public static final ResourceID ACTIVITY_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "ActivitySchema");
	public static final ResourceID ACTELLON_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "ActellonSchema");
	public static final ResourceID RELATION_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "RelationSchema");
	
	public static final ResourceID HAS_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "hasSchema");
	public static final ResourceID HAS_STATEMENT_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "hasStatementSchema");
	public static final ResourceID HAS_ACTELLON_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "hasActellonSchema");
	public static final ResourceID HAS_ATTRIBUTE_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "hasAttributeSchema");
	public static final ResourceID HAS_RELATION_SCHEMA = new SimpleResourceID(NAMESPACE_URI, "hasRelationSchema");
	public static final ResourceID SUB_SCHEMA_OF = new SimpleResourceID(NAMESPACE_URI, "subSchemaOf");
	public static final ResourceID CONTAINS_CHOICE_ELEMENTS = new SimpleResourceID(NAMESPACE_URI, "containsChoiceElements");
	
	// -- CONSTRAINTS -------------------------------------
	
	public static final ResourceID HAS_PROPER_NAME = new SimpleResourceID(NAMESPACE_URI, "hasProperName");
	public static final ResourceID HAS_FORENAME = new SimpleResourceID(NAMESPACE_URI, "hasForename");
	public static final ResourceID HAS_SURNAME = new SimpleResourceID(NAMESPACE_URI, "hasSurname");
	public static final ResourceID HAS_MIDDLE_NAME = new SimpleResourceID(NAMESPACE_URI, "hasMiddleName");
	public static final ResourceID HAS_NAME_PART = new SimpleResourceID(NAMESPACE_URI, "hasNamePart");
	public static final ResourceID HAS_BRAND_NAME = new SimpleResourceID(NAMESPACE_URI, "hasBrandName");
	
	// -- DATA_STRUCTURE ----------------------------------
	
	public static final ResourceID IS_PREDECESSOR_OF = new SimpleResourceID(NAMESPACE_URI, "isPredecessorOf");
	public static final ResourceID IS_SUCCESSOR_OF = new SimpleResourceID(NAMESPACE_URI, "isSuccessorOf");
	
}
