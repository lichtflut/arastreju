package org.arastreju.bindings.memory.conversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.bindings.memory.index.MockIndex;
import org.arastreju.sge.index.LuceneQueryBuilder;
import org.arastreju.sge.index.QNResolver;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.abstracts.AbstractConversation;
import org.arastreju.sge.spi.abstracts.WorkingContext;

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
	public MemConversation(final WorkingContext conversationContext) {
		super(conversationContext);
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
	protected QNResolver getQNResolver() {
		throw new NotYetImplementedException();
	}
}
