/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.ogm.impl.enhance;

import net.sf.cglib.proxy.Enhancer;

/**
 * <p>
 *  Enhancer for entity nodes.
 * </p>
 *
 * <p>
 * 	Created Jan 18, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class EntityEnhancer {
	
	public EnhancedEntityNode proxy(Object original) {
		final Enhancer e = new Enhancer();
		e.setSuperclass(original.getClass());
		e.setInterfaces(new Class[] { EnhancedEntityNode.class });
		return (EnhancedEntityNode) e.create();
	}

}
