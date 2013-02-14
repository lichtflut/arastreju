package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.Conversation;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.index.ArasIndexerImpl;
import org.arastreju.sge.index.IndexSearcher;
import org.arastreju.sge.index.LuceneQueryBuilder;
import org.arastreju.sge.index.QNResolver;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.persistence.TxResultAction;
import org.arastreju.sge.query.Query;
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
public abstract class AbstractConversation implements Conversation {

	private final WorkingContext context;

	// ----------------------------------------------------

    /**
     * Constructor.
     * @param context The context of this conversation.
     */
	public AbstractConversation(WorkingContext context) {
		this.context = context;
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

	// -- Semantic Graph operations -----------------------

	@Override
	public void attach(final SemanticGraph graph) {
		assertActive();
		context.getTxProvider().doTransacted(new TxResultAction<SemanticGraph>() {
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

	// -- Query support -----------------------------------

	@Override
	public Query createQuery() {
        assertActive();
        IndexSearcher searcher = new ArasIndexerImpl(context, context.getIndexProvider());
		return new LuceneQueryBuilder( searcher, getQNResolver());
	}

    // ----------------------------------------------------

	@Override
	public WorkingContext getConversationContext() {
		return context;
	}

    @Override
    public TransactionControl beginTransaction() {
        return context.getTxProvider().begin();
    }

	@Override
	public void close() {
		context.clear();
	}

	// ----------------------------------------------------

    /**
     * To be overridden by concrete classes.
     * @return The resolver for qualified names.
     */
    protected abstract QNResolver getQNResolver();

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
        if (!context.isActive()) {
            throw new IllegalStateException("Conversation already closed.");
        }
    }

}
