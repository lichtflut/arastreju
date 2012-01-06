/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.index;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.bindings.neo4j.impl.AssocKeeperAccess;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Cache for attached resources.
 * </p>
 *
 * <p>
 * 	Created Dec 23, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class AttachedResourcesCache {
	
	private SoftReference<Map<QualifiedName, ResourceNode>> registerReference;
	
	private SoftReference<Map<QualifiedName, NeoAssociationKeeper>> keeperRegisterReference;
	
	private Logger logger = LoggerFactory.getLogger(AttachedResourcesCache.class);
	
	// ----------------------------------------------------
	
	/**
	 * @param qn The resource's qualified name.
	 * @return The resource or null;
	 */
	public ResourceNode getNode(QualifiedName qn) {
		 final ResourceNode node = getNodeMap().get(qn);
		 if (node != null && !node.isAttached()) {
			 logger.warn("found detached node in cache: " + node);
			 remove(qn);
			 return null;
		 }
		 return node;
	}
	
	public AssociationKeeper getAssociationKeeper(QualifiedName qn) {
		return getKeeperMap().get(qn);
	}
	
	/**
	 * @param qn The resource's qualified name.
	 * @param resource
	 */
	public void put(QualifiedName qn, ResourceNode resource) {
		getNodeMap().put(qn, resource);
		getKeeperMap().put(qn, AssocKeeperAccess.getNeoAssociationKeeper(resource));
	}
	
	/**
	 * @param qn The resource's qualified name.
	 */
	public void remove(QualifiedName qn) {
		getNodeMap().remove(qn);
		getKeeperMap().remove(qn);
	}
	
	/**
	 * Clear the cache.
	 */
	public void clear() {
		getNodeMap().clear();
		getKeeperMap().clear();
	}
	
	// ----------------------------------------------------
	
	private synchronized Map<QualifiedName, ResourceNode> getNodeMap() {
		if (registerReference == null || registerReference.get() == null) {
			final Map<QualifiedName, ResourceNode> map = new HashMap<QualifiedName, ResourceNode>(1000);
			registerReference = new SoftReference<Map<QualifiedName, ResourceNode>>(map);
		}
		return registerReference.get();
	}
	
	private synchronized Map<QualifiedName, NeoAssociationKeeper> getKeeperMap() {
		if (keeperRegisterReference == null || keeperRegisterReference.get() == null) {
			final Map<QualifiedName, NeoAssociationKeeper> map = new HashMap<QualifiedName, NeoAssociationKeeper>(1000);
			keeperRegisterReference = new SoftReference<Map<QualifiedName, NeoAssociationKeeper>>(map);
		}
		return keeperRegisterReference.get();
	}

}
