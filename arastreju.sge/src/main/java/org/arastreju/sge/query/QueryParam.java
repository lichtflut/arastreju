/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Nov 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class QueryParam {

	private final String name;
	
	private final Object value;
	
	// -----------------------------------------------------

	/**
	 * Constructor.
	 * @param name
	 * @param value
	 */
	public QueryParam(final String name, final Object value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Constructor.
	 * @param name
	 * @param value
	 */
	public QueryParam(final ResourceID name, final Object value) {
		this(name.getQualifiedName(), value);
	}
	
	/**
	 * @param name
	 * @param value
	 */
	public QueryParam(QualifiedName name, final Object value) {
		this(name.toURI(), value);
	}

	// -----------------------------------------------------

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name + "=" + value;
	}
	
}
