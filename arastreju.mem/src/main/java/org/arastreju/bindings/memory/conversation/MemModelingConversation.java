package org.arastreju.bindings.memory.conversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.abstracts.AbstractModelingConversation;

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

    private final MemConversationContext conversationContext;

    // ----------------------------------------------------

    public MemModelingConversation(MemConversationContext conversationContext) {
        this.conversationContext = conversationContext;
    }

    // ----------------------------------------------------

    @Override
    public Query createQuery() {
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



    @Override
    public void attach(SemanticGraph graph) {
        throw new NotYetImplementedException();
    }

    @Override
    public void detach(SemanticGraph graph) {
        throw new NotYetImplementedException();
    }

    // ----------------------------------------------------

    @Override
    public ConversationContext getConversationContext() {
        return conversationContext;
    }

    @Override
    public TransactionControl beginTransaction() {
        throw new NotYetImplementedException();
    }

    @Override
    public void close() {
        conversationContext.close();
    }

    // ----------------------------------------------------

    @Override
    protected void assertActive() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
