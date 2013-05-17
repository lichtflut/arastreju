/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.spi.impl;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.ConversationController;
import org.arastreju.sge.spi.GraphDataConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Abstract base class for an ArasterjuGate.
 * </p>
 *
 * <p>
 *  Created 06.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class ArastrejuGateImpl implements ArastrejuGate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArastrejuGateImpl.class);

    private final GraphDataConnection connection;

    private final DomainIdentifier domainIdentifier;

    // ----------------------------------------------------

    /**
     * Create and open a new gate.
     * @param connection The connection to the graph data store.
     * @param domainIdentifier The gate context.
     */
	public ArastrejuGateImpl(GraphDataConnection connection, DomainIdentifier domainIdentifier) {
        this.connection = connection;
        this.domainIdentifier = domainIdentifier;
	}

    // ----------------------------------------------------

    @Override
    public Conversation startConversation() {
        return startConversation(domainIdentifier.getInitialContext(), domainIdentifier.getInitialContext());
    }

    @Override
    public Conversation startConversation(Context primary, Context... readContexts) {
        // Wire conversation context and controller
        ConversationContextImpl ctx = new ConversationContextImpl();
        ConversationController controller = newController(connection, ctx);
        ctx.setContexResolver(new ContextResolverImpl(controller));

        // Set initial contexts to be resolved by conversation context
        ctx.setPrimaryContext(primary);
        ctx.setReadContexts(readContexts);

        LOGGER.debug("New conversation context {} started.", ctx.getID());
        return newConversation(controller);
    }

    @Override
    public void close() {
        connection.close();
    }

    // ----------------------------------------------------

    protected Conversation newConversation(ConversationController ctx) {
        return new ConversationImpl(ctx);
    }

    protected ConversationController newController(GraphDataConnection connection, ConversationContextImpl ctx) {
        return new ConversationControllerImpl(connection, ctx);
    }

}
