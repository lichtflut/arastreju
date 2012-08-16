/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb;

/**
 * <p>
 *  RRdb specific extension of AbstractArastrejuGate. 
 * </p>
 *
 * <p>
 * 	Created 23.07.2012
 * </p>
 *
 * @author Raphael Esterle
 */

import java.sql.Connection;

import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.Organizer;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.abstracts.AbstractArastrejuGate;

public class RdbGate extends AbstractArastrejuGate {

	private Connection con;

	// ----------------------------------------------------

	protected RdbGate(Connection con, DomainIdentifier identifier) {
		super(identifier);
		this.con = con;
	}

	// ----------------------------------------------------

	@Override
	public ModelingConversation startConversation() {
		RdbConversationContext ctx = new RdbConversationContext(con,
				getDomainIdentifier().getStorage());
		initContext(ctx);
		return new RdbModelingConversation(ctx);
	}

	@Override
	public Organizer getOrganizer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
