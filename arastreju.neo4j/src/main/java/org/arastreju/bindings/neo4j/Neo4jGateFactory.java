/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.spi.ArastrejuGateFactory;
import org.arastreju.sge.spi.GateContext;
import org.arastreju.sge.spi.GateInitializationException;

/**
 * <p>
 *  Neo4j specific Gate Factory.
 * </p>
 *
 * <p>
 * 	Created Jan 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class Neo4jGateFactory extends ArastrejuGateFactory {

	/* (non-Javadoc)
	 * @see org.arastreju.sge.spi.AbstractSpiGateFactory#create()
	 */
	@Override
	public ArastrejuGate create(final GateContext ctx) throws GateInitializationException {
		return new Neo4jGate();
	}

}
