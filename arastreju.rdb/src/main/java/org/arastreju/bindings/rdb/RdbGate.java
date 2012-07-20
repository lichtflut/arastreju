package org.arastreju.bindings.rdb;

import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.Organizer;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.abstracts.AbstractArastrejuGate;

public class RdbGate extends AbstractArastrejuGate {

	private RdbConnectionProvider connectionProvider;

	protected RdbGate(RdbConnectionProvider connectionProvider, DomainIdentifier identifier) {
		super(identifier);
		this.connectionProvider = connectionProvider;
		
		String storageName = identifier.getStorage();
	}

	@Override
	public ModelingConversation startConversation() {
		RdbConversationContext ctx = new RdbConversationContext(connectionProvider);
		initContext(ctx);
		return new RdbModelingConversation(ctx);
	}

	@Override
	public Organizer getOrganizer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
