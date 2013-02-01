package org.arastreju.sge.model.associations;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.naming.QualifiedName;
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
public abstract class AttachedAssociationKeeper extends AbstractAssociationKeeper {

    private final QualifiedName qn;

    private WorkingContext context;

    // ----------------------------------------------------

    protected AttachedAssociationKeeper(QualifiedName qn) {
        this.qn = qn;
    }

    protected AttachedAssociationKeeper(QualifiedName qn, Set<Statement> associations) {
        super(associations);
        this.qn = qn;
    }

    // ----------------------------------------------------

    public QualifiedName getQualifiedName() {
        return qn;
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
        if (isAttached()) {
            getAssociations().remove(assoc);
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

}
