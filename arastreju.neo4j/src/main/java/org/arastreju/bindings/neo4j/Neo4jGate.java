/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import java.io.IOException;

import org.arastreju.bindings.neo4j.impl.NeoDataStore;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ContextManager;
import org.arastreju.sge.IdentityManagement;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.TypeSystem;
import org.arastreju.sge.spi.GateInitializationException;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

/**
 * <p>
 * Neo4j specific implementation of {@link ArastrejuGate}.
 * </p>
 * 
 * <p>
 * Created Jan 4, 2011
 * </p>
 * 
 * @author Oliver Tigges
 */
public class Neo4jGate implements ArastrejuGate {

	private NeoDataStore neo4jDataStore;

	// -----------------------------------------------------

	/**
	 * Initialize default gate.
	 */
	public Neo4jGate() throws GateInitializationException {
		try {
			this.neo4jDataStore = new NeoDataStore();
		} catch (IOException e) {
			throw new GateInitializationException(e);
		}
	}

	// -----------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.arastreju.sge.ArastrejuGate#startConversation()
	 */
	public ModelingConversation startConversation() {
		return new Neo4jModellingConversation(neo4jDataStore);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.arastreju.sge.ArastrejuGate#getTypeSystem()
	 */
	public TypeSystem getTypeSystem() {
		throw new NotYetImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.arastreju.sge.ArastrejuGate#getContextManager()
	 */
	public ContextManager getContextManager() {
		throw new NotYetImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.arastreju.sge.ArastrejuGate#getIdentityManagement()
	 */
	public IdentityManagement getIdentityManagement() {
		throw new NotYetImplementedException();
	}

}
