/*
 /*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.arastreju.bindings.rdb.jdbc.Column;
import org.arastreju.bindings.rdb.jdbc.TableOperations;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AbstractAssociationKeeper;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private final Logger logger = LoggerFactory.getLogger(RdbAssosiationKeeper.class);
	
	private final ResourceID id;
	private RdbConversationContext ctx;

	public RdbAssosiationKeeper(ResourceID id, RdbConversationContext ctx) {
		super();
		this.id = id;
		this.ctx = ctx;
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
			if(object.isResourceNode()){
				inserts.put(Column.OBJECT.value(), assoc.getObject().asResource().toURI());
				inserts.put(Column.TYPE.value(), ElementaryDataType.RESOURCE.toString());
			}
			else{
				
				ValueNode vNode = assoc.getObject().asValue();
				
				inserts.put(Column.TYPE.value(), vNode.getDataType().toString());
				inserts.put(Column.OBJECT.value(), vNode.getStringValue());
			}
				
			
			TableOperations.insert(ctx.getConnectionProvider().getConnection(), ctx.getTable(), inserts);
		}
	}

	@Override
	public boolean removeAssociation(final Statement assoc) {
//		if(rdb.deleteAssoc(assoc)){
//			return super.removeAssociation(assoc);
//		}
		return false;
	}

	@Override
	protected void resolveAssociations() {
		HashMap<String, String> conditions = new HashMap<String, String>();
		conditions.put(Column.SUBJECT.value(), id.toURI());
		ArrayList<Map<String, String>> stms = TableOperations.select(ctx.getConnectionProvider().getConnection(), ctx.getTable(), conditions);
		for (Map<String, String> map : stms) {
			SNOPS.id(new QualifiedName(map.get(Column.SUBJECT.value())));
			SNOPS.id(new QualifiedName(map.get(Column.PREDICATE.value())));
			
			String sObj = map.get(Column.OBJECT.value());
			ElementaryDataType type = ElementaryDataType.valueOf(map.get(Column.TYPE.value().trim()));
			
			switch(type){
				case RESOURCE:
					QualifiedName qn = new QualifiedName(sObj);
				SNOPS.id(qn);
				new SNResource(qn);
					break;
				case INTEGER:
				new SNValue(type, new BigInteger(sObj));
					break;
				case DECIMAL:
				new SNValue(type, new BigDecimal(sObj));
					break;
				case DATE:
				new SNValue(type, new Date(Long.parseLong(sObj)));
					break;
				case BOOLEAN:
					boolean b = false;
					if(sObj.equals("1"))
						b = true;
				new SNValue(type, b);
					break;
				default:
				new SNValue(type, sObj);
					break;
			}
		}

	}
	

}