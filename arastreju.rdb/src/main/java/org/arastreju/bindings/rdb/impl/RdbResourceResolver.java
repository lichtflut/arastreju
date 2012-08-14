/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.arastreju.bindings.rdb.Cache;
import org.arastreju.bindings.rdb.RdbAssosiationKeeper;
import org.arastreju.bindings.rdb.RdbConnectionProvider;
import org.arastreju.bindings.rdb.RdbConversationContext;
import org.arastreju.bindings.rdb.SNResourceRdb;
import org.arastreju.bindings.rdb.jdbc.TableOperations;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.ResourceResolver;

/**
 * <p>
 * [DESCRIPTION]
 * </p>
 * 
 * <p>
 * Created 14.08.2012
 * </p>
 * 
 * @author Raphael Esterle
 */
public class RdbResourceResolver implements ResourceResolver {

	private final RdbConversationContext ctx;
	private final Cache cache;
	private final RdbConnectionProvider conProvieder;

	public RdbResourceResolver(RdbConversationContext ctx) {
		this.ctx = ctx;
		this.cache = ctx.getCache();
		this.conProvieder = ctx.getConnectionProvider();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceNode resolve(ResourceID rid) {

		ResourceNode node = rid.asResource();
		AssociationKeeper keeper;

		if (node.isAttached())
			return node;
		if (cache.contains(rid.getQualifiedName())) {
			keeper = cache.get(rid.getQualifiedName());
		} else
			keeper = new RdbAssosiationKeeper(rid, ctx);
		cache.add(rid.getQualifiedName(), keeper);
		return new SNResourceRdb(rid.getQualifiedName(), keeper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceNode findResource(QualifiedName qn) {
		if (cache.contains(qn)) {
			return new SNResourceRdb(qn, cache.get(qn));
		} else {
			Connection con = conProvieder.getConnection();
			boolean b = TableOperations.hasOutgoingAssosiations(con, ctx.getTable(), qn);
			conProvieder.close(con);
			if(b)
				return new SNResourceRdb(qn, new RdbAssosiationKeeper(SNOPS.id(qn), ctx));
		}
		return null;
	}

}
