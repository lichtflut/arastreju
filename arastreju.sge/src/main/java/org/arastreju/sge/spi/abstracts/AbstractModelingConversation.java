package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.persistence.TxResultAction;
import org.arastreju.sge.spi.AssocKeeperAccess;

/**
 * <p>
 * Abstract base for modeling conversations.
 * </p>
 *
 * <p>
 *  Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractModelingConversation implements ModelingConversation {

	private final ConversationContext conversationContext;

	// ----------------------------------------------------

	public AbstractModelingConversation(final ConversationContext conversationContext) {
		this.conversationContext = conversationContext;
	}

	/**
	 * @deprecated Use other constructor with conversation.
	 */
	@Deprecated
	public AbstractModelingConversation() {
		this.conversationContext = null;
	}

	// ----------------------------------------------------

	@Override
	public void addStatement(final Statement stmt) {
		assertActive();
		final ResourceNode subject = resolve(stmt.getSubject());
		SNOPS.associate(subject, stmt.getPredicate(), stmt.getObject(), stmt.getContexts());
	}

	@Override
	public boolean removeStatement(final Statement stmt) {
		assertActive();
		final ResourceNode subject = resolve(stmt.getSubject());
		return SNOPS.remove(subject, stmt.getPredicate(), stmt.getObject());
	}

	// ----------------------------------------------------

	@Override
	public void attach(final SemanticGraph graph) {
		assertActive();
		conversationContext.getTxProvider().doTransacted(new TxResultAction<SemanticGraph>() {
			@Override
			public SemanticGraph execute() {
				for(Statement stmt : graph.getStatements()) {
					final ResourceNode subject = resolve(stmt.getSubject());
					SNOPS.associate(subject, stmt.getPredicate(), stmt.getObject(), stmt.getContexts());
				}
				return graph;
			}
		});
	}

	@Override
	public void detach(final SemanticGraph graph) {
		assertActive();
		for(SemanticNode node : graph.getNodes()){
			if (node.isResourceNode() && node.asResource().isAttached()){
				detach(node.asResource());
			}
		}
	}

	// ----------------------------------------------------

	@Override
	public ConversationContext getConversationContext() {
		return conversationContext;
	}

	@Override
	public void close() {
		conversationContext.clear();
	}

	@Override
	public TransactionControl beginTransaction() {
		return conversationContext.getTxProvider().begin();
	}

	// ----------------------------------------------------

    /**
     * Check if the given node is attached to this conversation.
     */
    protected boolean isAttached(ResourceNode resource) {
        AssociationKeeper given = AssocKeeperAccess.getInstance().getAssociationKeeper(resource);
        ConversationContext givenCtx = given.getConversationContext();
        return givenCtx != null && givenCtx.equals(getConversationContext());
    }

    protected void assertActive() {
        if (!conversationContext.isActive()) {
            throw new IllegalStateException("Conversation already closed.");
        }
    }

}
