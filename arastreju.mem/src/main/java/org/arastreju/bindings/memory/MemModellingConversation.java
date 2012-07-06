package org.arastreju.bindings.memory;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.query.Query;

/**
 * <p>
 * DESCRITPION.
 * </p>
 * <p/>
 * <p>
 * Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemModellingConversation implements ModelingConversation {
    @Override
    public void addStatement(Statement stmt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean removeStatement(Statement stmt) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Query createQuery() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResourceNode findResource(QualifiedName qn) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResourceNode resolve(ResourceID resourceID) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attach(ResourceNode node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void detach(ResourceNode node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reset(ResourceNode node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void remove(ResourceID id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void attach(SemanticGraph graph) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void detach(SemanticGraph graph) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ConversationContext getConversationContext() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TransactionControl beginTransaction() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
