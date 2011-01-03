/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import java.util.HashMap;
import java.util.Map;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Registry for Arastreju resources.
 * </p>
 *
 * <p>
 * 	Created Sep 9, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class ResourceRegistry {
	
	private final Map<QualifiedName, ResourceNode> resourceMap = new HashMap<QualifiedName, ResourceNode>();
	
	// -----------------------------------------------------
	
	public boolean contains(final QualifiedName qn){
		return resourceMap.containsKey(qn);
	}
	
	public void register(final ResourceNode resource){
		resourceMap.put(resource.getQualifiedName(), resource);
	}
	
	public void unregister(final ResourceNode resource){
		resourceMap.remove(resource.getQualifiedName());
	}
	
	public ResourceNode get(final QualifiedName qn){
		return resourceMap.get(qn);
	}

	// -----------------------------------------------------
	
	public void clear(){
		resourceMap.clear();
	}
}
