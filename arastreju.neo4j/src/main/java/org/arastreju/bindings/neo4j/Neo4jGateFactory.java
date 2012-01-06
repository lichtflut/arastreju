/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.spi.ArastrejuGateFactory;
import org.arastreju.sge.spi.GateContext;
import org.arastreju.sge.spi.GateInitializationException;

/**
 * <p>
 *  Neo4j specific Gate Factory.
 * </p>
 *
 * <p>
 * 	Created Jan 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class Neo4jGateFactory extends ArastrejuGateFactory {

	/**
	 * Constructor.
	 */
	public Neo4jGateFactory(final ArastrejuProfile profile) {
		super(profile);
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArastrejuGate create(final GateContext ctx) throws GateInitializationException {
		final Neo4jGate gate = new Neo4jGate(ctx);
		getProfile().onOpen(gate);
		return gate;
	}

}
