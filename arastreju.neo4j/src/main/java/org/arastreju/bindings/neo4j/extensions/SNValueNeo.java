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
package org.arastreju.bindings.neo4j.extensions;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.SNValue;
import org.neo4j.graphdb.Node;

/**
 * <p>
 *  Simple extension of {@link SNValue} for Neo data nodes.
 * </p>
 *
 * <p>
 * 	Created Jan 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class SNValueNeo extends SNValue implements NeoConstants {

	/**
	 * Creates a new {@link SNValue} based on a Neo4j {@link Node}.
	 * @param neoNode The Neo4j Node.
	 */
	public SNValueNeo(final Node neoNode){
		super(getDatatype(neoNode), neoNode.getProperty(PROPERTY_VALUE));
	}
	
	// -----------------------------------------------------
	
	/**
	 * @param neoNode The Neo Node.
	 * @return The corresponding datatype.
	 */
	private static ElementaryDataType getDatatype(final Node neoNode) {
		final String datatypeName = (String) neoNode.getProperty(PROPERTY_DATATYPE);
		return ElementaryDataType.valueOf(datatypeName);
	}

}
