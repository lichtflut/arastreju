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
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.WorkingContext;

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
        WorkingContext ctx = newWorkingContext(connection);
        ConversationContext cc = ctx.getConversationContext();
        cc.setPrimaryContext(primary);
        cc.setReadContexts(readContexts);
        return newConversation(ctx);
    }

    @Override
    public void close() {
        connection.close();
    }

    // ----------------------------------------------------

    protected Conversation newConversation(WorkingContext ctx) {
        return new ConversationImpl(ctx);
    }

    protected WorkingContext newWorkingContext(GraphDataConnection connection) {
        return new WorkingContextImpl(connection);
    }

}
