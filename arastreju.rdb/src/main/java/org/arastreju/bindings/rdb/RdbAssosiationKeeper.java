/*
 /*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

//	private final Logger logger = LoggerFactory
//			.getLogger(RdbAssosiationKeeper.class);

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

			HashMap<String, String> inserts = new HashMap<String, String>();
			inserts.put(Column.SUBJECT.value(), assoc.getSubject().toURI());
			inserts.put(Column.PREDICATE.value(), assoc.getPredicate().toURI());
			SemanticNode object = assoc.getObject();
			if (object.isResourceNode()) {
				inserts.put(Column.OBJECT.value(), assoc.getObject()
						.asResource().toURI());
				inserts.put(Column.TYPE.value(),
						ElementaryDataType.RESOURCE.toString());
			} else {

				ValueNode vNode = assoc.getObject().asValue();

				inserts.put(Column.TYPE.value(), vNode.getDataType().toString());
				inserts.put(Column.OBJECT.value(), vNode.getStringValue());
			}

			Connection con = conProvieder.getConnection();
			TableOperations.insert(con, ctx.getTable(), inserts);
			conProvieder.close(con);
		}
	}

	@Override
	public boolean removeAssociation(final Statement assoc) {
		// if(rdb.deleteAssoc(assoc)){
		// return super.removeAssociation(assoc);
		// }
		return false;
	}

	@Override
	protected void resolveAssociations() {
		HashMap<String, String> conditions = new HashMap<String, String>();
		conditions.put(Column.SUBJECT.value(), id.toURI());
		Connection con = conProvieder.getConnection();
		ArrayList<Map<String, String>> stms = TableOperations.select(con,
				ctx.getTable(), conditions);
		;
		conProvieder.close(con);

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

}