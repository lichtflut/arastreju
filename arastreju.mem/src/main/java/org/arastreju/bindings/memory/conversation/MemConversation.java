package org.arastreju.bindings.memory.conversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.bindings.memory.keepers.MemAssocKeeper;
import org.arastreju.bindings.memory.nodes.SNMemResource;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.AssocKeeperAccess;
import org.arastreju.sge.spi.abstracts.AbstractConversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(MemConversation.class);

    // ----------------------------------------------------

	public MemConversation(final MemConversationContext conversationContext) {
		super(conversationContext);
	}

	// ----------------------------------------------------

	@Override
	public ResourceNode findResource(final QualifiedName qn) {
        MemAssocKeeper existing = getConversationContext().find(qn);
        if (existing != null) {
            return new SNMemResource(qn, existing);
        }
        return null;
    }

	@Override
	public ResourceNode resolve(final ResourceID resourceID) {
        MemAssocKeeper existing = getConversationContext().find(resourceID.getQualifiedName());
        if (existing != null) {
            return new SNMemResource(resourceID.getQualifiedName(), existing);
        } else {
            AssociationKeeper created = getConversationContext().create(resourceID.getQualifiedName());
            return new SNMemResource(resourceID.getQualifiedName(), created);
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
        MemAssocKeeper existing = getConversationContext().find(node.getQualifiedName());
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
		throw new NotYetImplementedException();
	}

	@Override
	public void remove(final ResourceID id) {
		throw new NotYetImplementedException();
	}

    @Override
    public Query createQuery() {
        throw new NotYetImplementedException();
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

}
