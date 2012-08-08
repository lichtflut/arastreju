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
import java.util.Set;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.abstracts.AbstractModelingConversation;

public class RdbModelingConversation extends AbstractModelingConversation {

	private RdbConversationContext context;
	private Field assocKeeperField;
	
	// ----------------------------------------------------
	
	public RdbModelingConversation(RdbConversationContext conversationContext) {
		super(conversationContext);
		context = conversationContext;
		try {
			assocKeeperField = SNResource.class.getDeclaredField("associationKeeper");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceNode findResource(QualifiedName qn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceNode resolve(ResourceID resourceID) {
		Cache cache = context.getCache();
		ResourceNode node = resourceID.asResource();
		QualifiedName qn = resourceID.getQualifiedName();
		if(node.isAttached())
			return node;
		if(cache.contains(resourceID.getQualifiedName())){
			setAssociationKeeper(node, cache.get(qn));
		}else{
			setAssociationKeeper(node, new RdbAssosiationKeeper(resourceID, context));
		}
		return node;
	}

	@Override
	public void attach(ResourceNode node) {
		if(node.isAttached())
			return;
		if(context.getCache().contains(node.getQualifiedName()));
			//merge
		else{
			Set<Statement> copy = node.getAssociations();
			RdbAssosiationKeeper keeper = new RdbAssosiationKeeper(node, context);
			setAssociationKeeper(node, keeper);

			for (Statement smt : copy) {
				keeper.addAssociation(smt);
			}
			System.out.println("add: "+node.getQualifiedName());
			context.getCache().add(node.getQualifiedName(), keeper);
		}
			
	}

	@Override
	public void detach(ResourceNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset(ResourceNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(ResourceID id) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void assertActive() {
		// TODO Auto-generated method stub

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
