package org.arastreju.bindings.memory.conversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.bindings.memory.keepers.MemAssocKeeper;
import org.arastreju.bindings.memory.nodes.SNMemResource;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.AssocKeeperAccess;
import org.arastreju.sge.spi.abstracts.AbstractModelingConversation;
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
public class MemModelingConversation extends AbstractModelingConversation {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemModelingConversation.class);

    // ----------------------------------------------------

	public MemModelingConversation(final MemConversationContext conversationContext) {
		super(conversationContext);
	}

	// ----------------------------------------------------

	@Override
	public ResourceNode findResource(final QualifiedName qn) {
        MemAssocKeeper foundInConversation = getConversationContext().getAssociationKeeper(qn);
        if (foundInConversation != null) {
            return new SNMemResource(qn, foundInConversation);
        }
        // TODO: look in storage

        return null;
    }

	@Override
	public ResourceNode resolve(final ResourceID resourceID) {
        MemAssocKeeper foundInConversation = getConversationContext().getAssociationKeeper(resourceID.getQualifiedName());
        if (foundInConversation != null) {
            return new SNMemResource(resourceID.getQualifiedName(), foundInConversation);
        }
        // TODO: look in storage

        return null;
	}

	@Override
	public void attach(final ResourceNode node) {
        if (isAttached(node)) {
            // already attached to this conversation - nothing to do
            return;
        }
        AssociationKeeper given = AssocKeeperAccess.getInstance().getAssociationKeeper(node);
        MemAssocKeeper foundInConversation = getConversationContext().getAssociationKeeper(node.getQualifiedName());
        if (foundInConversation != null) {
            // TODO: merge
        } else {
            // TODO: create new node
        }
	}

	@Override
	public void detach(final ResourceNode node) {
		throw new NotYetImplementedException();
	}

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
