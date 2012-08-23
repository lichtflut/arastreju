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

import org.arastreju.bindings.rdb.jdbc.ConnectionWraper;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.Organizer;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.spi.abstracts.AbstractArastrejuGate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class RdbGate extends AbstractArastrejuGate {

	private final ComboPooledDataSource dataSource;
	private final String table;
	private RdbConversationContext ctx;

	// ----------------------------------------------------

	protected RdbGate(ComboPooledDataSource dataSource, DomainIdentifier identifier) {
		super(identifier);
		this.dataSource = dataSource;
		table = identifier.getStorage();
	}

	// ----------------------------------------------------

	@Override
	public ModelingConversation startConversation() {
		ctx = new RdbConversationContext(new ConnectionWraper(dataSource, table));
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
		ctx.close();
		dataSource.close();
	}

}
