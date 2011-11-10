/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

import java.util.List;


/**
 * <p>
 *  Expression of a {@link Query}.
 * </p>
 *
 * <p>
 * 	Created Nov 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface QueryExpression {
	
	boolean isLeaf();

	QueryOperator getOperator();

	QueryParam getQueryParam();

	List<QueryExpression> getChildren();

	void add(QueryExpression exp);

}