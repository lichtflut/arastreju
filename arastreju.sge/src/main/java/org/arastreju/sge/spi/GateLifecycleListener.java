/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.spi;

import org.arastreju.sge.ArastrejuGate;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jun 9, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface GateLifecycleListener {
	
	void onOpen(final ArastrejuGate gate);
	
	void onClose(final ArastrejuGate gate);

}
