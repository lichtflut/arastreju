/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.ogm.impl.enhance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.InvocationHandler;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;

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
public class ResourceNodeDelegate implements InvocationHandler, EnhancedEntityNode {

	private final Object entity;
	
	private final AnalyzedClass analyzedClass;
	
	private ResourceNode node;
	
	// ----------------------------------------------------
	
	/**
	 * @param node
	 */
	public ResourceNodeDelegate(ResourceNode node, Object entity, AnalyzedClass analyzedClass) {
		this.node = node;
		this.entity = entity;
		this.analyzedClass = analyzedClass;
	}
	
	// ----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(proxy, args);
	}

	/** 
	 * {@inheritDoc}
	 */
	public ResourceNode aras$getResourceNode() {
		return node;
	}

	/** 
	 * {@inheritDoc}
	 */
	public void aras$setResourceNode(ResourceNode node) {
		this.node = node;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public ResourceID aras$getID() {
		if (node != null) {
			return node;
		} else {
			return (ResourceID) getFieldValue(analyzedClass.obtainFieldForID());
		}
	}

	private Object getFieldValue(final Field field) {
		try {
			return field.get(entity);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
