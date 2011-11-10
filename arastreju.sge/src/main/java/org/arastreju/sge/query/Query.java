/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Query object.
 * </p>
 *
 * <p>
 * 	Created Nov 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Query {
	
	/**
	 * @param param
	 * @return
	 */
	Query add(QueryParam param);

	/**
	 * @return
	 */
	Query and();

	/**
	 * @return
	 */
	Query or();

	/**
	 * @return
	 */
	Query not();

	/**
	 * @return
	 */
	Query beginAnd();

	/**
	 * @return
	 */
	Query beginOr();

	/**
	 * @return
	 */
	Query end();
	
	// -----------------------------------------------------

	QueryResult getResult();
	
	ResourceNode getSingleNode();

}
