/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.ogm;

import org.arastreju.sge.model.ResourceID;

/**
 * <p>
 *  Manager for entity nodes.
 * </p>
 *
 * <p>
 * 	Created Jan 18, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public interface EntityNodeManager {
	
	/**
	 * Find a entity by it's ID.
	 * @param id The id.
	 * @return The object or null if not found.
	 */
	Object find(ResourceID id);
	
	/**
	 * Find typed a entity by it's ID.
	 * @param type The class.
	 * @param id The id.
	 * @return The object or null if not found.
	 */
	<T> T find(Class<T> type, ResourceID id);
	
	/**
	 * Attach a entity to the semantic network. 
	 * @param entity The entity to be attached.
	 */
	void attach(Object entity);

}
