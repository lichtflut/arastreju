package org.arastreju.bindings.memory.conversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.bindings.memory.index.MockIndex;
import org.arastreju.sge.index.LuceneQueryBuilder;
import org.arastreju.sge.index.QNResolver;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.AssocKeeperAccess;
import org.arastreju.sge.spi.AttachedResourceNode;
import org.arastreju.sge.spi.abstracts.AbstractConversation;

import java.util.Set;

/**
 * <p>
 *  The conversation context.
 * </p>

 * <p>
 *  Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemConversation extends AbstractConversation {

    /**
     * Create a new conversation.
     * @param conversationContext The context of this conversation.
     */
	public MemConversation(final MemConversationContext conversationContext) {
		super(conversationContext);
	}

	// ----------------------------------------------------

	@Override
	public ResourceNode findResource(final QualifiedName qn) {
        AttachedAssociationKeeper existing = getConversationContext().find(qn);
        if (existing != null) {
            return new AttachedResourceNode(qn, existing);
        }
        return null;
    }

	@Override
	public ResourceNode resolve(final ResourceID resourceID) {
        AttachedAssociationKeeper existing = getConversationContext().find(resourceID.getQualifiedName());
        if (existing != null) {
            return new AttachedResourceNode(resourceID.getQualifiedName(), existing);
        } else {
            AssociationKeeper created = getConversationContext().create(resourceID.getQualifiedName());
            return new AttachedResourceNode(resourceID.getQualifiedName(), created);
        }
	}

	@Override
	public void attach(final ResourceNode node) {
        if (isAttached(node)) {
            // already attached to this conversation - nothing to do
            return;
        }
        AssocKeeperAccess accessor = AssocKeeperAccess.getInstance();
        AssociationKeeper given = accessor.getAssociationKeeper(node);
        AttachedAssociationKeeper existing = getConversationContext().find(node.getQualifiedName());
        if (existing != null) {
            accessor.merge(existing, given);
            accessor.setAssociationKeeper(node, existing);
        } else {
            AssociationKeeper created = getConversationContext().create(node.getQualifiedName());
            accessor.setAssociationKeeper(node, created);
        }
	}

	@Override
	public void detach(final ResourceNode node) {
        AssocKeeperAccess.getInstance().setAssociationKeeper(
                node, new DetachedAssociationKeeper(node.getAssociations()));
        getConversationContext().detach(node.getQualifiedName());
	}

    // ----------------------------------------------------

	@Override
	public void reset(final ResourceNode node) {
        if (isAttached(node)) {
            return;
        }
        AttachedAssociationKeeper existing = getConversationContext().find(node.getQualifiedName());
        if (existing != null) {
            AssocKeeperAccess.getInstance().setAssociationKeeper(node, existing);
        } else {
            throw new IllegalStateException("Detached node cannot be reset.");
        }
	}

	@Override
	public void remove(final ResourceID id) {
        getConversationContext().detach(id.getQualifiedName());
	}

    // ----------------------------------------------------

    @Override
    public Query createQuery() {
        return new LuceneQueryBuilder(new MockIndex(), new QNResolver() {
            @Override
            public ResourceNode resolve(QualifiedName qn) {
                return findResource(qn);
            }
        });
    }

    @Override
    public Set<Statement> findIncomingStatements(final ResourceID object) {
        throw new NotYetImplementedException();
    }

    // ----------------------------------------------------

    @Override
    public MemConversationContext getConversationContext() {
        return (MemConversationContext) super.getConversationContext();
    }

	@Override
	protected QNResolver getQNResolver() {
		throw new NotYetImplementedException();
	}
}
