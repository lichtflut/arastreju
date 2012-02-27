/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.io;

import org.arastreju.sge.ModelingConversation;
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
	
	private final ModelingConversation mc;
	
	// ----------------------------------------------------

	/**
	 * @param mc The modelling conversation.
	 */
	public StatementStorer(ModelingConversation mc) {
		this.mc = mc;
	}

	// ----------------------------------------------------

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onNewStatement(Statement stmt) {
		mc.addStatement(stmt);
	}

}
