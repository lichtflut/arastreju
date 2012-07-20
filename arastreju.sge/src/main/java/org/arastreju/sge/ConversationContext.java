/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.persistence.TxProvider;

/**
 * <p>
 *  Context of a {@link ModelingConversation}.
 * </p>
 *
 * <p>
 * 	Created Jun 7, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public interface ConversationContext {

	Context[] getReadContexts();

	Context getPrimaryContext();
	
	ConversationContext setPrimaryContext(Context context);
	
	ConversationContext setReadContexts(Context... contexts);

	void clear();

    // ----------------------------------------------------

    TxProvider getTxProvider();

}