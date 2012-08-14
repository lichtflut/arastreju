/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb;

/**
 * <p>
 *  RRdb specific extension of AbstractModelingConversation. 
 * </p>
 *
 * <p>
 * 	Created 23.07.2012
 * </p>
 *
 * @author Raphael Esterle
 */

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Set;

import org.arastreju.bindings.rdb.impl.RdbResourceResolver;
import org.arastreju.bindings.rdb.jdbc.TableOperations;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.abstracts.AbstractModelingConversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

public class RdbModelingConversation extends AbstractModelingConversation {

	private RdbConversationContext context;
	private Field assocKeeperField;
	private final Cache cache;
	private final RdbConnectionProvider conProvider;
	private final RdbResourceResolver resolver;

	// ----------------------------------------------------

	public RdbModelingConversation(RdbConversationContext conversationContext) {
		super(conversationContext);
		context = conversationContext;
		cache = context.getCache();
		conProvider = context.getConnectionProvider();
		resolver = new RdbResourceResolver(conversationContext);
		try {
			assocKeeperField = SNResource.class
					.getDeclaredField("associationKeeper");
			assocKeeperField.setAccessible(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

	}

	// ----------------------------------------------------

	@Override
	public Query createQuery() {
		return null;
	}

	@Override
	public ResourceNode findResource(QualifiedName qn) {
		
//		ResourceNode node = resolve(SNOPS.id(qn));
//		if (node.asResource().getAssociations().size() < 1)
//			return null;
		return resolver.findResource(qn);

	}

	@Override
	public ResourceNode resolve(ResourceID resourceID) {
		return resolver.resolve(resourceID);
	}

	@Override
	public void attach(ResourceNode node) {

		if (node.isAttached())
			return;
		if (cache.contains(node.getQualifiedName())) {
			AssociationKeeper newKeeper = cache.get(node.getQualifiedName());
			Set<Statement> oldAssocs = node.getAssociations();
			Set<Statement> newAssocs = newKeeper.getAssociations();
			for (Statement statement : oldAssocs) {
				if (!newAssocs.contains(statement))
					newKeeper.addAssociation(statement);
			}
			setAssociationKeeper(node, newKeeper);
		} else {
			Set<Statement> copy = node.getAssociations();
			RdbAssosiationKeeper keeper = new RdbAssosiationKeeper(node,
					context);
			setAssociationKeeper(node, keeper);
			for (Statement smt : copy) {
				keeper.addAssociation(smt);
			}
			cache.add(node.getQualifiedName(), keeper);
		}

	}

	@Override
	public void detach(ResourceNode node) {
		Set<Statement> copy = node.getAssociations();
		setAssociationKeeper(node, new DetachedAssociationKeeper(copy));
		cache.remove(node.getQualifiedName());
	}

	@Override
	public void reset(ResourceNode node) {
		throw new NotYetImplementedException();

	}

	@Override
	public void remove(ResourceID id) {
		// Delete all outgoing and incomming assosiations
		Connection con = conProvider.getConnection();
		TableOperations.deleteOutgoingAssosiations(con, context.getTable(),
				id.toURI());
		TableOperations.deleteIncommingAssosiations(con, context.getTable(),
				id.toURI());
		conProvider.close(con);

		// Remove the assosiationkeeper from cache.
		cache.remove(id.getQualifiedName());

		setAssociationKeeper(id.asResource(), new DetachedAssociationKeeper());

	}

	@Override
	protected void assertActive() {
		throw new NotYetImplementedException();
	}

	private void setAssociationKeeper(final ResourceNode node,
			final AssociationKeeper ak) {
		final ResourceNode resource = node.asResource();
		if (!(resource instanceof SNResource)) {
			throw new IllegalArgumentException(
					"Cannot set AssociationKeeper for class: "
							+ node.getClass());
		}
		try {
			assocKeeperField.set(resource, ak);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
