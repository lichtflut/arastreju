/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.naming.QualifiedName;

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
	 * @return This.
	 */
	Query add(QueryParam param);
	
	/**
	 * @param name
	 * @param value
	 * @return This.
	 */
	Query addField(String name, Object value);
	
	/**
	 * Constructor.
	 * @param name
	 * @param value
	 * @return This.
	 */
	Query addField(ResourceID name, Object value);
	
	/**
	 * @param name
	 * @param value
	 */
	Query addField(QualifiedName name, Object value);
	
	/**
	 * @param name
	 * @param value
	 */
	Query addURI(String term);
	
	/**
	 * @param name
	 * @param value
	 */
	Query addValue(String term);

	// ----------------------------------------------------
	
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
	
}
