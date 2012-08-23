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

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.arastreju.bindings.rdb.impl.RdbResourceResolver;
import org.arastreju.bindings.rdb.jdbc.ConnectionWraper;
import org.arastreju.bindings.rdb.jdbc.TableOperations;
import org.arastreju.bindings.rdb.tx.JdbcTxProvider;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxAction;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.AssocKeeperAccess;
import org.arastreju.sge.spi.abstracts.AbstractModelingConversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

public class RdbModelingConversation extends AbstractModelingConversation {

	private final RdbConversationContext context;
	private final AssocKeeperAccess assocKeeperAccess;
	private final Cache cache;
	private final RdbResourceResolver resolver;

	// ----------------------------------------------------

	public RdbModelingConversation(RdbConversationContext conversationContext) {
		super(conversationContext);
		context = conversationContext;
		cache = context.getCache();
		resolver = new RdbResourceResolver(conversationContext);
		assocKeeperAccess = AssocKeeperAccess.getInstance();
		
	}
	
	public RdbModelingConversation(ConnectionWraper cw){
		this(new RdbConversationContext(cw));
	}

	// ----------------------------------------------------

	@Override
	public Query createQuery() {
		return null;
	}

	@Override
	public ResourceNode findResource(QualifiedName qn) {
		return resolver.findResource(qn);

	}

	@Override
	public ResourceNode resolve(ResourceID resourceID) {
		return resolver.resolve(resourceID);
	}

	@Override
	public void attach(ResourceNode node) {
		
		// Node is attached
		if (node.isAttached())
			return;
		AssociationKeeper newKeeper;
		if (cache.contains(node.getQualifiedName())) {
			newKeeper = cache.get(node.getQualifiedName());
		}else{
			newKeeper = new RdbAssosiationKeeper(node, context);
		}
		merge(newKeeper, node);

	}

	@Override
	public void detach(ResourceNode node) {
		Set<Statement> copy = node.getAssociations();
		assocKeeperAccess.setAssociationKeeper(node, new DetachedAssociationKeeper(copy));
		cache.remove(node.getQualifiedName());
	}

	@Override
	public void reset(ResourceNode node) {
		if(node.isAttached())
			return;
		AssociationKeeper keeper = cache.get(node.getQualifiedName());
		if(null==keeper)
			keeper = new RdbAssosiationKeeper(SNOPS.id(node), context);
		assocKeeperAccess.setAssociationKeeper(node, keeper);
	}

	@Override
	public void remove(final ResourceID id) {
		// Delete all outgoing and incomming assosiations
		JdbcTxProvider txProvider = (JdbcTxProvider) context.getTxProvider();
		txProvider.doTransacted(new TxAction() {
			
			@Override
			public void execute() {
				try {
					TableOperations.deleteOutgoingAssosiations(context.getConnection(), context.getTable(),
							id.toURI());
					TableOperations.deleteIncommingAssosiations(context.getConnection(), context.getTable(),
							id.toURI());
				} catch (SQLException e) {
					throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_QUERY_ERROR, e.getMessage());
				}
			}
		});

		// Remove the assosiationkeeper from cache.
		cache.remove(id.getQualifiedName());
		
		assocKeeperAccess.setAssociationKeeper(id.asResource(), new DetachedAssociationKeeper());

	}
	
	@Override
	protected void assertActive() {
		throw new NotYetImplementedException();
	}

	/**
	 * Merges all associations from the 'changed' node to the 'attached' keeper and put's keeper in 'changed'.
	 * @param attached The currently attached keeper for this resource.
	 * @param changed An unattached node referencing the same resource.
	 */
	protected void merge(final AssociationKeeper attached, final ResourceNode changed) {
		final Set<Statement> currentAssocs = new HashSet<Statement>(attached.getAssociations());
		final AssociationKeeper detached = assocKeeperAccess.getAssociationKeeper(changed);
		for (Statement toBeRemoved : detached.getAssociationsForRemoval()) {
			attached.removeAssociation(toBeRemoved);
		}
		for(Statement assoc : detached.getAssociations()){
			if (!currentAssocs.contains(assoc)){
				attached.addAssociation(assoc);
			}
		}
		assocKeeperAccess.setAssociationKeeper(changed, attached);
	}

}
