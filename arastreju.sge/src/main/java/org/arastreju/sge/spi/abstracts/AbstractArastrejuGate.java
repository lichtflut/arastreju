package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.context.DomainIdentifier;

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
public abstract class AbstractArastrejuGate implements ArastrejuGate {

    private final DomainIdentifier domainIdentifier;

    // ----------------------------------------------------

    protected AbstractArastrejuGate(DomainIdentifier domainIdentifier) {
        this.domainIdentifier = domainIdentifier;
    }

    protected DomainIdentifier getDomainIdentifier() {
        return domainIdentifier;
    }

    protected void initContext(ConversationContext cc) {
        if (domainIdentifier.getInitialContext() != null) {
            cc.setWriteContext(domainIdentifier.getInitialContext());
            cc.setReadContexts(domainIdentifier.getInitialContext());
        }
    }

}
