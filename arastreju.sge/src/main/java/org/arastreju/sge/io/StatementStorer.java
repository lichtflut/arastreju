/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.io;

import org.arastreju.sge.Conversation;
import org.arastreju.sge.model.Statement;

/**
 * <p>
 *  Listener that stores the statements directly.
 * </p>
 *
 * <p>
 * 	Created Feb 25, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class StatementStorer implements ReadStatementListener {
	
	private final Conversation conversation;
	
	// ----------------------------------------------------

	/**
	 * @param conversation The modelling conversation.
	 */
	public StatementStorer(Conversation conversation) {
		this.conversation = conversation;
	}

	// ----------------------------------------------------

	@Override
	public void onNewStatement(Statement stmt) {
		conversation.addStatement(stmt);
	}

}
