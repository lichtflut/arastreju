/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.spi;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.security.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Initializer of a domain.
 * </p>
 *
 * <p>
 * 	Created Jan 11, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class DomainInitializer {
	
	private final Logger logger = LoggerFactory.getLogger(DomainInitializer.class);
	
	// ----------------------------------------------------
	
	public void run(ArastrejuGate gate, String domain) {
		logger.info("Initializing domain " + domain);
		final Domain masterDomain = gate.getOrganizer().getMasterDomain();
		if (masterDomain == null) {
			logger.info("Creating master domain object for " + domain);
			gate.getOrganizer().initMasterDomain(domain);
		}
		final ModelingConversation mc = gate.startConversation();
		mc.close();
	}

}
