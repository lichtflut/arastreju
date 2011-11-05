/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

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
	 * @return The root expression.
	 */
	QueryExpression getRoot();
	
}
