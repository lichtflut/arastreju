/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import java.util.HashMap;
import java.util.Map;

import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Sep 9, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class NodeRegistry {
	
	private final Map<QualifiedName, SNResource> resourceMap = new HashMap<QualifiedName, SNResource>();
	
	// -----------------------------------------------------
	
	public boolean contains(final QualifiedName qn){
		return resourceMap.containsKey(qn);
	}
	
	public void register(final SNResource resource){
		resourceMap.put(resource.getQualifiedName(), resource);
	}
	
	public SNResource get(final QualifiedName qn){
		return resourceMap.get(qn);
	}

	// -----------------------------------------------------
	
	public void clear(){
		resourceMap.clear();
	}
}
