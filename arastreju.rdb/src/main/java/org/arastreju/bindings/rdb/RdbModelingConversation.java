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

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.abstracts.AbstractModelingConversation;

public class RdbModelingConversation extends AbstractModelingConversation {

	private RdbConversationContext context;
	
	// ----------------------------------------------------
	
	public RdbModelingConversation(RdbConversationContext conversationContext) {
		super(conversationContext);
		context = conversationContext;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void attach(ResourceNode node) {
		// TODO Auto-generated method stub

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

}
