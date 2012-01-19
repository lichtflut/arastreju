/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.ogm.impl.enhance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.arastreju.ogm.annotations.NodeID;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jan 18, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class AnalyzedClass {
	
	private final Class<?> clazz;
	
	// ----------------------------------------------------
	
	/**
	 * Constrcutor.
	 */
	public AnalyzedClass(final Class<?> clazz) {
		this.clazz = clazz;
	}
	
	// ----------------------------------------------------
	
	public Method obtainGetter(final String predicate) {
		return null;
	}
	
	public Method obtainGetterForID() {
		return null;
	}
	
	public Field obtainFieldForID() {
		for(Field field :clazz.getFields()) {
			final NodeID nodeID = field.getAnnotation(NodeID.class);
			if (nodeID != null) {
				return field;
			}
		}
		return null;
	}

}
