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

import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.persistence.ResourceResolver;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.spi.AssocKeeperAccess;
import org.arastreju.sge.spi.AssociationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  Manages creation and removal of associations.
 * </p>
 *
 * <p>
 *  Created Feb. 14, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class AssociationManager implements AssociationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssociationManager.class);

    // ----------------------------------------------------

    private final List<AssociationListener> listeners = new ArrayList<>();

    private final ResourceResolver resolver;

    private final TransactionControl tx;

    // ----------------------------------------------------

    /**
     * Constructor.
     * @param resolver A resolver for resources.
     * @param tx A transaction control handle.
     */
    public AssociationManager(ResourceResolver resolver, TransactionControl tx) {
        this.resolver = resolver;
        this.tx = tx;
    }

    // ----------------------------------------------------

    public AssociationManager register(AssociationListener... listeners) {
        Collections.addAll(this.listeners, listeners);
        return this;
    }

    // ----------------------------------------------------

    /**
     * Create a new association.
     * @param keeper The subject of the statement.
     * @param stmt The statement.
     */
    public void addAssociation(AttachedAssociationKeeper keeper, Statement stmt) {
        if (keeper.getAssociations().contains(stmt)) {
            LOGGER.debug("Keeper {} already has statement {}", keeper.getPhysicalID(), stmt);
            return;
        }
        Statement attached = attach(stmt);
        keeper.addAssociationDirectly(attached);
        for (AssociationListener listener : listeners) {
            listener.onCreate(attached);
        }
    }

    /**
     * Remove an association.
     * @param keeper The subject of the statement.
     * @param stmt The statement.
     */
    public void removeAssociation(AttachedAssociationKeeper keeper, Statement stmt) {
        Statement attached = attach(stmt);
        keeper.removeAssociationDirectly(attached);
        for (AssociationListener listener : listeners) {
            listener.onRemove(attached);
        }
    }

    // ----------------------------------------------------

    @Override
    public void onCreate(Statement stmt) {
        addAssociation(getKeeper(stmt.getSubject()), stmt);
    }

    @Override
    public void onRemove(Statement stmt) {
        removeAssociation(getKeeper(stmt.getSubject()), stmt);
    }

    // ----------------------------------------------------

    private Statement attach(Statement stmt) {
        final ResourceNode subject = resolver.resolve(stmt.getSubject());
        final ResourceNode predicate = resolver.resolve(stmt.getPredicate());
        final SemanticNode object = resolve(stmt.getObject());
        return new DetachedStatement(subject, predicate, object, stmt.getMetaInfo());
    }

    private SemanticNode resolve(final SemanticNode node) {
        if (node.isResourceNode()) {
            return resolver.resolve(node.asResource());
        } else {
            return node;
        }
    }

    private AttachedAssociationKeeper getKeeper(ResourceID rid) {
        ResourceNode node = resolver.resolve(rid);
        return (AttachedAssociationKeeper) AssocKeeperAccess.getInstance().getAssociationKeeper(node);
    }

}
