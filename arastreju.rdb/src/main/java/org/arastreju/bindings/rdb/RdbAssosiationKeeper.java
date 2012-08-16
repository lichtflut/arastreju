/*
 /*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.arastreju.bindings.rdb.jdbc.Column;
import org.arastreju.bindings.rdb.jdbc.TableOperations;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AbstractAssociationKeeper;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 * Simple Database Operations.
 * </p>
 * 
 * <p>
 * Created Feb 14, 2012
 * </p>
 * 
 * @author Raphael Esterle
 */

public class RdbAssosiationKeeper extends AbstractAssociationKeeper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private final Logger logger = LoggerFactory
	// .getLogger(RdbAssosiationKeeper.class);

	private final ResourceID id;
	private RdbConversationContext ctx;
	private final RdbConnectionProvider conProvieder;

	public RdbAssosiationKeeper(ResourceID id, RdbConversationContext ctx) {
		super();
		this.id = id;
		this.ctx = ctx;
		conProvieder = ctx.getConnectionProvider();
	}

	public Set<Statement> getAssociationsForRemoval() {
		return null;
	}

	public boolean isAttached() {
		return true;
	}

	@Override
	public void addAssociation(final Statement assoc) {

		if (!getAssociations().contains(assoc)) {

			super.addAssociation(assoc);

			Map<String, String> objectStr = objectAsString(assoc.getObject());

			String subject = assoc.getSubject().toURI();
			String predicate = assoc.getPredicate().toURI();
			String object = objectStr.get(Column.OBJECT.value());
			String type = objectStr.get(Column.TYPE.value());

			Connection con = conProvieder.getConnection();
			TableOperations.insert(con, ctx.getTable(), subject, predicate,
					object, type);
			conProvieder.returnConection(con);
		}
	}

	@Override
	public boolean removeAssociation(final Statement assoc) {
		
		Map<String, String> objectStr = objectAsString(assoc.getObject());

		String subject = assoc.getSubject().toURI();
		String predicate = assoc.getPredicate().toURI();
		String object = objectStr.get(Column.OBJECT.value());
		
		Connection con = conProvieder.getConnection();
		TableOperations.deleteAssosiation(con, ctx.getTable(), subject, predicate, object);
		conProvieder.returnConection(con);
		return false;
	}

	@Override
	protected void resolveAssociations() {
		Connection con = conProvieder.getConnection();
		List<Map<String, String>> stms = TableOperations
				.getOutgoingAssosiations(con, ctx.getTable(),
						id.getQualifiedName());
		conProvieder.returnConection(con);

		for (Map<String, String> map : stms) {

			ResourceID predicate = SNOPS.id(new QualifiedName(map
					.get(Column.PREDICATE.value().toUpperCase())));
			String sObj = map.get(Column.OBJECT.value().toUpperCase());
			ElementaryDataType type = ElementaryDataType.valueOf(map
					.get(Column.TYPE.value().trim().toUpperCase()));
			SemanticNode object = null;

			switch (type) {
			case RESOURCE:
				QualifiedName qn = new QualifiedName(sObj);
				object = new SNResource(qn);
				break;
			case INTEGER:
				object = new SNValue(type, new BigInteger(sObj));
				break;
			case DECIMAL:
				object = new SNValue(type, new BigDecimal(sObj));
				break;
			case DATE:
				object = new SNValue(type, new Date(Long.parseLong(sObj)));
				break;
			case BOOLEAN:
				boolean b = false;
				if (sObj.equals("1"))
					b = true;
				object = new SNValue(type, b);
				break;
			default:
				object = new SNValue(type, sObj);
				break;
			}

			getAssociations().add(
					new DetachedStatement(this.id, predicate, object, ctx
							.getReadContexts()));
		}

	}

	private Map<String, String> objectAsString(SemanticNode node) {
		Map<String, String> hm = new HashMap<String, String>();
		if (node.isResourceNode()) {
			hm.put(Column.OBJECT.value(), node.asResource().toURI());
			hm.put(Column.TYPE.value(), ElementaryDataType.RESOURCE.toString());
		} else {
			ValueNode vNode = node.asValue();
			hm.put(Column.OBJECT.value(), vNode.getStringValue());
			hm.put(Column.TYPE.value(), vNode.getDataType().toString());
		}
		return hm;
	}

}