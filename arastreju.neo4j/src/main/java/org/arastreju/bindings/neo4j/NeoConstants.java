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
package org.arastreju.bindings.neo4j;

import org.neo4j.graphdb.Relationship;

/**
 * <p>
 *  Constants for Arastreju Neo binding.
 * </p>
 *
 * <p>
 * 	Created Oct 11, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface NeoConstants {
	
	String PROPERTY_URI = "resource-uri";
	
	String PROPERTY_VALUE = "value";
	
	String PROPERTY_DATATYPE = "datatype";
	
	/**
	 * Attribute of a {@link Relationship}.
	 */
	String CONTEXT_URI = "context-uri";
	
	/**
	 * Attribute of a {@link Relationship}.
	 */
	String PREDICATE_URI = "predicate-uri";
	
}
