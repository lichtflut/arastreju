package org.arastreju.bindings.memory.conversation;

import java.util.Set;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.spi.abstracts.AbstractModelingConversation;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

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

	public MemModelingConversation(final MemConversationContext conversationContext) {
		super(conversationContext);
	}

	// ----------------------------------------------------

	@Override
	public Query createQuery() {
		throw new NotYetImplementedException();
	}

	@Override
	public Set<Statement> findIncomingStatements(final ResourceID object) {
		throw new NotYetImplementedException();
	}

	@Override
	public ResourceNode findResource(final QualifiedName qn) {
		throw new NotYetImplementedException();
	}

	@Override
	public ResourceNode resolve(final ResourceID resourceID) {
		throw new NotYetImplementedException();
	}

	@Override
	public void attach(final ResourceNode node) {
		throw new NotYetImplementedException();
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

	// ----------------------------------------------------

	@Override
	protected void assertActive() {
	}

}
