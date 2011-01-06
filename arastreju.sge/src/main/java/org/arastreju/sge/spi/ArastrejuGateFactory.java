/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.spi;

import org.arastreju.sge.ArastrejuGate;

/**
 * <p>
 *  Factory for creation of implementation specific {@link ArastrejuGate}s.
 * </p>
 *
 * <p>
 * 	Created Jan 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class ArastrejuGateFactory {

	public abstract ArastrejuGate create(final GateContext ctx) throws GateInitializationException;
	
}
