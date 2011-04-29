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
package org.arastreju.sge;

import org.arastreju.sge.security.Identity;
import org.arastreju.sge.spi.ArastrejuGateFactory;
import org.arastreju.sge.spi.GateContext;

/**
 * <p>
 * Central API class for obtaining an {@link ArastrejuGate}.
 * </p>
 * 
 * <p>
 * Created Jan 4, 2011
 * </p>
 * 
 * @author Oliver Tigges
 */
public final class Arastreju {

	private final static Arastreju DEFAULT_INSTANCE = new Arastreju();

	// -----------------------------------------------------

	private final String factoryClass;
	private final ArastrejuGateFactory factory;

	// -----------------------------------------------------

	/**
	 * Get the Arastreju instance for the default profile.
	 * @return the instance.
	 */
	public static Arastreju getInstance() {
		return DEFAULT_INSTANCE;
	}
	
	/**
	 * Get a Arastreju instance for a given profile.
	 * A profile describes the binding to the graph store (e.g. Neo4j).
	 * For future use!
	 * @param profile The name/path of the Arastreju profile.
	 * @return the instance
	 */
	public static Arastreju getInstance(final String profile) {
		return DEFAULT_INSTANCE;
	}

	// -----------------------------------------------------

	/**
	 * Login into the Gate using given username and credentials.
	 * @param username The unique username.
	 * @param credentials The users credentials.
	 * @return The corresponding {@link ArastrejuGate}.
	 */
	public ArastrejuGate login(final String username, final String credentials) {
		return factory.create(new GateContext(username, credentials));
	}

	/**
	 * Obtain the root context. Use Carefully! No login will be performed but
	 * the ArastrejuGate will be used in root context.
	 * 
	 * <p>
	 *  Specific providers can deny root access. Or allow root access only as long
	 *  as user 'root' has no credential set. 
	 * </p>
	 * 
	 * @return The ArastrejuGate for the root context.
	 */
	public ArastrejuGate rootContext() {
		return rootContext(null);
	}
	
	/**
	 * Obtain the root context. Use Carefully! ArastrejuGate will be used in root context.
	 * 
	 * <p>
	 *  Specific providers can deny root access. 
	 * </p>
	 * 
	 * @return The ArastrejuGate for the root context.
	 */
	public ArastrejuGate rootContext(final String credentials) {
		return factory.create(new GateContext(Identity.ROOT, credentials));
	}

	// -----------------------------------------------------

	/**
	 * Private constructor.
	 */
	private Arastreju() {
		this("META-INF/arastreju.profile");
	}
	
	/**
	 * Private constructor.
	 * @param profile path to the profile file.
	 */
	private Arastreju(final String profile) {
		// TODO: read initialization from profile
		this.factoryClass = "org.arastreju.bindings.neo4j.Neo4jGateFactory";
		try {
			this.factory = (ArastrejuGateFactory) Class.forName(
					factoryClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
