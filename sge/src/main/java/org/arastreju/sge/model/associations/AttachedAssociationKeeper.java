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
package org.arastreju.sge.model.associations;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.ConversationController;
import org.arastreju.sge.spi.PhysicalNodeID;

import java.util.Set;

/**
 * <p>
 *  Base class for attached association keepers.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class AttachedAssociationKeeper extends AbstractAssociationKeeper {

    private final QualifiedName qn;

    private final PhysicalNodeID physicalID;

    private ConversationController controller;

    // ----------------------------------------------------

    public AttachedAssociationKeeper(QualifiedName qn, PhysicalNodeID physicalID) {
        this.qn = qn;
        this.physicalID = physicalID;
    }

    public AttachedAssociationKeeper(QualifiedName qn, PhysicalNodeID physicalID, Set<Statement> associations) {
        super(associations);
        this.qn = qn;
        this.physicalID = physicalID;
    }

    // ----------------------------------------------------

    public QualifiedName getQualifiedName() {
        return qn;
    }

    public PhysicalNodeID getPhysicalID() {
        return physicalID;
    }

    @Override
    public ConversationContext getConversationContext() {
        return controller.getConversationContext();
    }

    /**
     * Set the controller this association keeper is bound to.
     * If this keeper is detached, there will be no controller.
     * @return The controller or null.
     */
    public void setController(ConversationController controller) {
        this.controller = controller;
    }

    // ----------------------------------------------------

    @Override
    public void addAssociation(final Statement assoc) {
        if (getAssociations().contains(assoc)) {
            return;
        }
        if (isAttached()) {
            controller.addAssociation(this, assoc);
        } else {
            super.addAssociation(assoc);
        }
    }

    @Override
    public boolean removeAssociation(final Statement assoc) {
        if (!getAssociations().contains(assoc)) {
            return false;
        }
        if (isAttached()) {
            return controller.removeAssociation(this, assoc);
        } else {
            return super.removeAssociation(assoc);
        }
    }

    @Override
    protected void resolveAssociations() {
        if (isAttached()) {
            controller.resolveAssociations(this);
        } else {
            throw new IllegalStateException("This node is no longer attached. Cannot resolve associations.");
        }
    }

    // ----------------------------------------------------

    /**
     * Add an association directly to the set, without resolving.
     * @param assoc The association to add.
     */
    public void addAssociationDirectly(Statement assoc) {
        getAssociationsDirectly().add(assoc);
    }

    /**
     * Remove a association directly to the set, without resolving.
     * @param assoc The association to remove.
     */
    public void removeAssociationDirectly(Statement assoc) {
        getAssociationsDirectly().remove(assoc);
    }

    /**
     * Called when the underlying physical node has been changed in another conversation.
     * The state of this node must be reset to trigger a reload later.
     */
    public void notifyChanged() {
        reset();
    }

    // ----------------------------------------------------

    @Override
    public boolean isAttached() {
        return controller != null && controller.isActive();
    }

    /**
     * Detach this association keeper.
     */
    public void detach() {
        markResolved();
        controller = null;
    }

    // ----------------------------------------------------

    /**
     * Called when being serialized --> Replace by detached association keeper.
     * @return A Detached Association Keeper.
     */
    private Object writeReplace() {
        return new DetachedAssociationKeeper(getAssociationsDirectly());
    }

}
