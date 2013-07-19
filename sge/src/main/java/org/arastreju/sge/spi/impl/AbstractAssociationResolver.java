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

import org.arastreju.sge.context.Accessibility;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.StatementMetaInfo;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNContext;
import org.arastreju.sge.persistence.ResourceResolver;
import org.arastreju.sge.spi.AssociationResolver;
import org.arastreju.sge.spi.ContextResolver;
import org.arastreju.sge.spi.ConversationController;
import org.arastreju.sge.spi.uow.ResourceResolverImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * <p>
 *  Abstract base for implementation for AssociationResolver.
 * </p>
 *
 * <p>
 *  Created May 17, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractAssociationResolver implements AssociationResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAssociationResolver.class);

    private final ConversationController controller;

    private final ResourceResolver resourceResolver;

    private final ContextResolver contextResolver;

    // ----------------------------------------------------

    protected AbstractAssociationResolver(ConversationController controller) {
        this.controller = controller;
        this.resourceResolver = new ResourceResolverImpl(controller);
        this.contextResolver = new ContextResolverImpl(controller);
    }

    // ----------------------------------------------------

    protected ConversationController controller() {
        return controller;
    }

    protected Context[] readContexts() {
        return controller().getConversationContext().getReadContexts();
    }

    // ----------------------------------------------------

    /**
     * Check if at least one of the given contexts may be read with current read contexts.
     * @param stmtContexts The contexts of a statement to be tested.
     * @return true if read is allowed.
     */
    protected boolean regardContext(Context[] stmtContexts) {
        if (stmtContexts.length == 0) {
            LOGGER.debug("Statement has no context.");
            return true;
        }
        Context[] readContexts = readContexts();
        for (Context stmtContext : stmtContexts) {
            Context accessContext = contextResolver.resolve(stmtContext).getAccessContext();
            if (isPublicVisible(accessContext)) {
                return true;
            }
            for (Context readContext : readContexts) {
                if (readContext.equals(accessContext)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected ResourceNode resolve(String uri) {
        return resourceResolver.resolve(new SimpleResourceID(uri));
    }

    protected StatementMetaInfo createMetaInfo(Context[] stmtContexts, Date date) {
        return new StatementMetaInfo(stmtContexts, date);
    }

    protected StatementMetaInfo createMetaInfo(Context[] stmtContexts, long date) {
        return createMetaInfo(stmtContexts, new Date(date));
    }

    // ----------------------------------------------------

    private boolean isPublicVisible(Context ctx) {
        SNContext resolved = contextResolver.resolve(ctx);
        return resolved != null && Accessibility.PUBLIC.equals(resolved.getVisibility());
    }

}
