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

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.model.nodes.views.SNContext;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.AttachedResourceNode;
import org.arastreju.sge.spi.ContextResolver;
import org.arastreju.sge.spi.ConversationController;

/**
 * <p>
 *  Implementation of context resolver.
 * </p>
 *
 * <p>
 *  Created May 17, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class ContextResolverImpl implements ContextResolver {

    private final ConversationController controller;

    // ----------------------------------------------------

    public ContextResolverImpl(ConversationController controller) {
        this.controller = controller;
    }

    // ----------------------------------------------------

    @Override
    public SNContext resolve(QualifiedName qn) {
        AttachedAssociationKeeper keeper = controller.find(qn);
        if (keeper != null) {
            return SNContext.from(new AttachedResourceNode(qn, keeper));
        }
        return new SNContext(qn);
    }

    @Override
    public SNContext resolve(Context ctx) {
        return resolve(ctx.getQualifiedName());
    }

}
