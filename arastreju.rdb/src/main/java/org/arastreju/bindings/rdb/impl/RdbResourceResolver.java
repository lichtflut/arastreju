/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb.impl;

import org.arastreju.bindings.rdb.Cache;
import org.arastreju.bindings.rdb.RdbAssosiationKeeper;
import org.arastreju.bindings.rdb.RdbConversationContext;
import org.arastreju.bindings.rdb.SNResourceRdb;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.ResourceResolver;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created 14.08.2012
 * </p>
 *
 * @author Raphael Esterle

 */
public class RdbResourceResolver implements ResourceResolver {

	private final RdbConversationContext ctx;
	private final Cache cache;
	
	public RdbResourceResolver(RdbConversationContext ctx) {
		this.ctx=ctx;
		this.cache = ctx.getCache();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ResourceNode resolve(ResourceID rid) {
		
		ResourceNode node = rid.asResource();
		AssociationKeeper keeper;
		
		if(node.isAttached())
			return node;
		if(cache.contains(rid.getQualifiedName())){
			keeper = cache.get(rid.getQualifiedName());
		}else
			keeper = new RdbAssosiationKeeper(rid, ctx);
		cache.add(rid.getQualifiedName(), keeper);
		return new SNResourceRdb(rid.getQualifiedName(), keeper);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ResourceNode findResource(QualifiedName qn) {
		// TODO Auto-generated method stub
		return null;
	}

}
