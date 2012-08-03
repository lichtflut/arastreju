package org.arastreju.bindings.memory.conversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.abstracts.AbstractModelingConversation;

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

    public MemModelingConversation(MemConversationContext conversationContext) {
        super(conversationContext);
    }

    // ----------------------------------------------------

    @Override
    public Query createQuery() {
        throw new NotYetImplementedException();
    }

    @Override
    public Set<Statement> findIncomingStatements(ResourceID object) {
        throw new NotYetImplementedException();
    }

    @Override
    public ResourceNode findResource(QualifiedName qn) {
        throw new NotYetImplementedException();
    }

    @Override
    public ResourceNode resolve(ResourceID resourceID) {
        throw new NotYetImplementedException();
    }

    @Override
    public void attach(ResourceNode node) {
        throw new NotYetImplementedException();
    }

    @Override
    public void detach(ResourceNode node) {
        throw new NotYetImplementedException();
    }

    @Override
    public void reset(ResourceNode node) {
        throw new NotYetImplementedException();
    }

    @Override
    public void remove(ResourceID id) {
        throw new NotYetImplementedException();
    }

    // ----------------------------------------------------

    @Override
    protected void assertActive() {
    }

}
