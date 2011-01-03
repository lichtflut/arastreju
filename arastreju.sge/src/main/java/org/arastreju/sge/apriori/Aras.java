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
 * A priori known URIs for Arastreju.
 * 
 * Created: 09.11.2009
 *
 * @author Oliver Tigges 
 */
public interface Aras {
	
	// -- TYPES -------------------------------------------
	
	public static final String NAMESPACE_URI = "http://arastreju.org/kernel#";
	public static final ResourceID ACTIVITY_CLASS = new SimpleResourceID(NAMESPACE_URI, "ActivityClass");
	public static final ResourceID PROPERTY_DECL = new SimpleResourceID(NAMESPACE_URI, "PropertyDeclaration");

	public static final ResourceID RELATION_CLASSIFIER = new SimpleResourceID(NAMESPACE_URI, "RelationClassifier");
	public static final ResourceID RELATION_LINK_TYPE = new SimpleResourceID(NAMESPACE_URI, "RelationLinkType");
	public static final ResourceID RELATION_PREPOSITIONAL = new SimpleResourceID(NAMESPACE_URI, "RelationPrepositional");
	public static final ResourceID RELATION_DIRECT = new SimpleResourceID(NAMESPACE_URI, "RelationDirect");
	public static final ResourceID IMPLICATION = new SimpleResourceID(NAMESPACE_URI, "Implication");
	public static final ResourceID EQUALIZE_IMPLICATION = new SimpleResourceID(NAMESPACE_URI, "EqualizeImplication");
	public static final ResourceID ASSOC_IMPLICATION = new SimpleResourceID(NAMESPACE_URI, "AssociateImplication");

	public static final ResourceID COUNTABLE = new SimpleResourceID(NAMESPACE_URI, "Countable");
	
	public static final ResourceID NAME_TYPE_FORNAME = new SimpleResourceID(NAMESPACE_URI, "Forename");
	public static final ResourceID NAME_TYPE_SURNAME = new SimpleResourceID(NAMESPACE_URI, "Surname");
	public static final ResourceID NAME_TYPE_MIDDLENAME = new SimpleResourceID(NAMESPACE_URI, "Middlename");
	public static final ResourceID NAME_TYPE_BRAND_NAME = new SimpleResourceID(NAMESPACE_URI, "BrandName");
	public static final ResourceID NAME_TYPE_FULL_NAME = new SimpleResourceID(NAMESPACE_URI, "Fullname");
	
	public static final ResourceID OBJECT_STATMENT = new SimpleResourceID("http://arastreju.org/grammar#", "ObjectStatement");
	
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
	
	// -- MODEL SELECTIONS --------------------------------
	
	public static final ResourceID MODEL_SELECTION = new SimpleResourceID(NAMESPACE_URI, "ModelSelection");
	public static final ResourceID DIAGRAM = new SimpleResourceID(NAMESPACE_URI, "Diagram");
	public static final ResourceID REGISTER = new SimpleResourceID(NAMESPACE_URI, "Register");
	public static final ResourceID CONTEXT = new SimpleResourceID(NAMESPACE_URI, "Context");
	public static final ResourceID SCENARIO_CONTEXT = new SimpleResourceID(NAMESPACE_URI, "ScenarioContext");
	
	public static final ResourceID CONTAINS = new SimpleResourceID(NAMESPACE_URI, "contains");
	public static final ResourceID SELECTS_BY_QUERY = new SimpleResourceID(NAMESPACE_URI, "selectsByQuery");
	public static final ResourceID SELECTS_BY_NAMESPACE = new SimpleResourceID(NAMESPACE_URI, "selectsByNamespace");
	public static final ResourceID HAS_DEFAULT_NAMESPACE = new SimpleResourceID(NAMESPACE_URI, "hasDefaultNamespace");
	public static final ResourceID IS_IN_CONTEXT = new SimpleResourceID(NAMESPACE_URI, "isInContext");
	
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
	
	public static final ResourceID HAS_CONSTRAINT = new SimpleResourceID(NAMESPACE_URI, "hasConstraint");
	public static final ResourceID HAS_TYPE_CONSTRAINT = new SimpleResourceID(NAMESPACE_URI, "hasTypeConstraint");
	public static final ResourceID HAS_ROLE_CONSTRAINT = new SimpleResourceID(NAMESPACE_URI, "hasRoleConstraint");
	public static final ResourceID HAS_CASUS_CONSTRAINT = new SimpleResourceID(NAMESPACE_URI, "hasCasusConstraint");
	public static final ResourceID HAS_PRONOUN_CONSTRAINT = new SimpleResourceID(NAMESPACE_URI, "hasPronounConstraint");
	public static final ResourceID HAS_PREPOSITION_CONSTRAINT = new SimpleResourceID(NAMESPACE_URI, "hasPrepositionConstraint");
	public static final ResourceID HAS_REL_SUBJECT_TYPE_CONSTRAINT = new SimpleResourceID(NAMESPACE_URI, "hasRelationSubjectTypeConstraint");
	public static final ResourceID HAS_REL_OBJECT_TYPE_CONSTRAINT = new SimpleResourceID(NAMESPACE_URI, "hasRelationObjectTypeConstraint");
	
	public static final ResourceID HAS_IMPLICATION = new SimpleResourceID(NAMESPACE_URI, "hasImplication");
	public static final ResourceID DESCRIBES = new SimpleResourceID(NAMESPACE_URI, "describes");
	public static final ResourceID HAS_MEDIA_DESC = new SimpleResourceID(NAMESPACE_URI, "hasMediaDescription");
	public static final ResourceID HAS_TERM = new SimpleResourceID(NAMESPACE_URI, "isReferencedByTerm");
	
	public static final ResourceID HAS_PROPERTY_DECL = new SimpleResourceID(NAMESPACE_URI, "hasPropertyDeclaration");
	public static final ResourceID DECLARES_PROPERTY = new SimpleResourceID(NAMESPACE_URI, "declaresProperty");
	
	public static final ResourceID HAS_PROPER_NAME = new SimpleResourceID(NAMESPACE_URI, "hasProperName");
	public static final ResourceID HAS_FORENAME = new SimpleResourceID(NAMESPACE_URI, "hasForename");
	public static final ResourceID HAS_SURNAME = new SimpleResourceID(NAMESPACE_URI, "hasSurname");
	public static final ResourceID HAS_MIDDLE_NAME = new SimpleResourceID(NAMESPACE_URI, "hasMiddleName");
	public static final ResourceID HAS_NAME_PART = new SimpleResourceID(NAMESPACE_URI, "hasNamePart");
	public static final ResourceID HAS_BRAND_NAME = new SimpleResourceID(NAMESPACE_URI, "hasBrandName");
	
}
