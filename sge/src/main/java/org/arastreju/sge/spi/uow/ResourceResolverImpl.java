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
package org.arastreju.sge.spi.uow;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.ResourceResolver;
import org.arastreju.sge.spi.AssocKeeperAccess;
import org.arastreju.sge.spi.AttachedResourceNode;
import org.arastreju.sge.spi.ConversationController;

import java.util.Set;

/**
 * <p>
 *  Standard implementation of ResourceResolver.
 * </p>
 *
 * <p>
 *  Created Feb. 15, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class ResourceResolverImpl implements ResourceResolver {

    private ConversationController context;

    // ----------------------------------------------------

    public ResourceResolverImpl(ConversationController context) {
        this.context = context;
    }

    // ----------------------------------------------------

    @Override
    public ResourceNode findResource(QualifiedName qn) {
        final AssociationKeeper keeper =  context.find(qn);
        if (keeper != null) {
            return new AttachedResourceNode(qn, keeper);
        } else {
            return null;
        }
    }

    @Override
    public ResourceNode resolve(ResourceID rid) {
        final ResourceNode node = rid.asResource();
        if (node.isAttached()){
            return node;
        } else {
            final QualifiedName qn = rid.getQualifiedName();
            final ResourceNode attached = findResource(qn);
            if (attached != null) {
                return attached;
            } else {
                return persist(node);
            }
        }
    }

    // ----------------------------------------------------

    protected ResourceNode persist(final ResourceNode node) {
        // 1st: create a corresponding Neo node and attach the Resource with the current context.
        AssociationKeeper keeper = context.create(node.getQualifiedName());

        // 2nd: retain copy of current associations
        final Set<Statement> copy = node.getAssociations();
        AssocKeeperAccess.getInstance().setAssociationKeeper(node, keeper);

        // 3rd: store all associations.
        for (Statement assoc : copy) {
            keeper.addAssociation(assoc);
        }

        return node;
    }


}
