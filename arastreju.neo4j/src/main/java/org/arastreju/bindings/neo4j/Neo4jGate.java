/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.bindings.neo4j;

import java.io.IOException;

import org.arastreju.bindings.neo4j.impl.GraphDataStore;
import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.bindings.neo4j.query.NeoQueryManager;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.IdentityManagement;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.Organizer;
import org.arastreju.sge.query.QueryManager;
import org.arastreju.sge.security.LoginException;
import org.arastreju.sge.spi.GateContext;
import org.arastreju.sge.spi.GateInitializationException;

/**
 * <p>
 * 	Neo4j specific implementation of {@link ArastrejuGate}.
 * </p>
 * 
 * <p>
 * 	Created Jan 4, 2011
 * </p>
 * 
 * @author Oliver Tigges
 */
public class Neo4jGate implements ArastrejuGate {
	
	private static final String KEY_GRAPH_DATA_STORE = "aras:neo4j:profile-object:graph-data-store";
	
	private final SemanticNetworkAccess sna;

	private final GateContext gateContext;

	// -----------------------------------------------------

	/**
	 * Initialize default gate.
	 * @param profile The Arastreju profile.
	 * @param ctx The gate context.
	 */
	public Neo4jGate(final GateContext ctx) throws GateInitializationException {
		this.gateContext = ctx;
		try {
			this.sna = obtainSemanticNetworkAccesss(ctx.getProfile());
			getIdentityManagement().login(ctx.getUsername(), ctx.getCredential());
		} catch (IOException e) {
			throw new GateInitializationException(e);
		} catch (LoginException e) {
			throw new GateInitializationException(e);
		}
	}

	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public ModelingConversation startConversation() {
		return new Neo4jModellingConversation(sna);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public QueryManager createQueryManager() {
		return new NeoQueryManager(sna, sna.getIndex());
	}

	/**
	 * {@inheritDoc}
	 */
	public Organizer getOrganizer() {
		return new NeoOrganizer(sna);
	}

	/**
	 * {@inheritDoc}
	 */

	public IdentityManagement getIdentityManagement() {
		return new NeoIdentityManagement(sna);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void close() {
	}
	
	// -----------------------------------------------------
	
	private synchronized SemanticNetworkAccess obtainSemanticNetworkAccesss(final ArastrejuProfile profile) throws IOException {
		GraphDataStore store = (GraphDataStore) profile.getProfileObject(KEY_GRAPH_DATA_STORE);
		if (store == null) { 
			store = createStore(profile);
			initStore(store, profile);
		}
		return new SemanticNetworkAccess(store);
	}

	private GraphDataStore createStore(final ArastrejuProfile profile) throws IOException {
		if (!isTemporaryStore()){
			return new GraphDataStore(profile.getProperty(ArastrejuProfile.ARAS_STORE_DIRECTORY));
		} else {
			return new GraphDataStore();
		}
	}
	
	private void initStore(final GraphDataStore store, final ArastrejuProfile profile) {
		profile.addListener(store);
		if (!isTemporaryStore()) {
			profile.setProfileObject(KEY_GRAPH_DATA_STORE, store);
		}
	}
	
	// -----------------------------------------------------
	
	private boolean isTemporaryStore() {
		return !gateContext.getProfile().isPropertyDefined(ArastrejuProfile.ARAS_STORE_DIRECTORY);
	}

}
