/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

import java.util.List;

import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Result of a {@link Query}.
 * </p>
 *
 * <p>
 * 	Created Nov 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface QueryResult extends Iterable<ResourceNode> {
	
	/**
	 * Close the query result.
	 * Should always be called to free resources.
	 */
	void close();
	
	/**
	 * Returns the size of the query result entries or -1 if the size is unknown.
	 * @return The size.
	 */
	int size();
	
	/**
	 * Converts the query result to a list.
	 * <p>Warning: This might be very expensive and memory consuming. Only use this method, if you really know
	 * 	the list result is not too large!.
	 * </p>
	 * @return The list (or maybe an OutOfMemoryException)
	 */
	List<ResourceNode> toList();

	/**
	 * Check if the result is empty.
	 * @return true if the result is empty.
	 */
	boolean isEmpty();

}
