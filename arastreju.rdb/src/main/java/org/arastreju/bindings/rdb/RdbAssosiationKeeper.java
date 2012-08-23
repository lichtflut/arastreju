/*
 /*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.arastreju.bindings.rdb.jdbc.Column;
import org.arastreju.bindings.rdb.jdbc.TableOperations;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
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
import org.arastreju.sge.persistence.TxAction;
import org.arastreju.sge.persistence.TxResultAction;

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

	public RdbAssosiationKeeper(ResourceID id, RdbConversationContext ctx) {
		super();
		this.id = id;
		this.ctx = ctx;
	}

	public Set<Statement> getAssociationsForRemoval() {
		return super.getAssociationsForRemoval();
	}

	public boolean isAttached() {
		return true;
	}

	@Override
	public void addAssociation(final Statement assoc) {

		if (!getAssociations().contains(assoc)) {

			super.addAssociation(assoc);

			Map<String, String> objectStr = objectAsString(assoc.getObject());

			final String subject = assoc.getSubject().toURI();
			final String predicate = assoc.getPredicate().toURI();
			final String object = objectStr.get(Column.OBJECT.value());
			final String type = objectStr.get(Column.TYPE.value());
			
			ctx.getTxProvider().doTransacted(new TxAction() {
				
				@Override
				public void execute() {
					try {
						TableOperations.insert(ctx.getConnection(), ctx.getTable(), subject, predicate,
								object, type);
					} catch (SQLException e) {
						throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR, e.getMessage());
					}
				}
			});
		}
	}

	@Override
	public boolean removeAssociation(final Statement assoc) {
		
		Map<String, String> objectStr = objectAsString(assoc.getObject());

		final String subject = assoc.getSubject().toURI();
		final String predicate = assoc.getPredicate().toURI();
		final String object = objectStr.get(Column.OBJECT.value());
		ctx.getTxProvider().doTransacted(new TxAction() {
			public void execute() {
				try {
					TableOperations.deleteAssosiation(ctx.getConnection(), ctx.getTable(), subject, predicate, object);
				} catch (SQLException e) {
					throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR, e.getMessage());
				}
				
			}
		});
		super.removeAssociation(assoc);
		return true;
	}

	@Override
	protected void resolveAssociations() {
		List<Map<String, String>> stms =ctx.getTxProvider().doTransacted(new TxResultAction<List<Map<String, String>>>() {

			@Override
			public List<Map<String, String>> execute() {
				try {
					return TableOperations
							.getOutgoingAssosiations(ctx.getConnection(), ctx.getTable(),
									id.getQualifiedName());
				} catch (SQLException e) {
					throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_IO_ERROR, e.getMessage());
				}
			}
		});
		

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