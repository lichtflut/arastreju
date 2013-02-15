package org.arastreju.sge.model.associations;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.PhysicalNodeID;
import org.arastreju.sge.spi.abstracts.WorkingContext;

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

    private WorkingContext context;

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
    public WorkingContext getConversationContext() {
        return context;
    }

    /**
     * Set the conversation context this association keeper is bound to.
     * If this keeper is detached, there will be no conversation context.
     * @return The conversation context or null.
     */
    public void setConversationContext(WorkingContext ctx) {
        this.context = ctx;
    }

    // ----------------------------------------------------

    @Override
    public void addAssociation(final Statement assoc) {
        if (getAssociations().contains(assoc)) {
            return;
        }
        if (isAttached()) {
            getConversationContext().addAssociation(this, assoc);
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
            return getConversationContext().removeAssociation(this, assoc);
        } else {
            return super.removeAssociation(assoc);
        }
    }

    @Override
    protected void resolveAssociations() {
        if (isAttached()) {
            getConversationContext().resolveAssociations(this);
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
     * Called when the underlying neo node has been changed in another conversation.
     * The state of this node must be reset to trigger a reload later.
     */
    public void notifyChanged() {
        reset();
    }

    // ----------------------------------------------------

    @Override
    public boolean isAttached() {
        return context != null && context.isActive();
    }

    /**
     * Detach this association keeper.
     */
    public void detach() {
        markResolved();
        context = null;
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
