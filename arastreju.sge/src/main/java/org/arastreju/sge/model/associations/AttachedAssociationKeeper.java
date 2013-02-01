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
