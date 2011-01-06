/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
	 * Creates a new {@link SNValue} based on a Neo {@link Node}.
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
