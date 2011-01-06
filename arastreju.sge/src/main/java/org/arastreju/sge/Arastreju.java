/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge;

import org.arastreju.sge.spi.ArastrejuGateFactory;

/**
 * <p>
 * Central API class for obtaining an {@link ArastrejuGate}.
 * </p>
 * 
 * <p>
 * Created Jan 4, 2011
 * </p>
 * 
 * @author Oliver Tigges
 */
public final class Arastreju {

	private final static Arastreju DEFAULT_INSTANCE = new Arastreju();

	// -----------------------------------------------------

	private final String defaultFactoryClass;
	private final ArastrejuGateFactory defaultFactory;

	// -----------------------------------------------------

	/**
	 * Get the Arastreju instance for the default profile.
	 * @return the instance.
	 */
	public static Arastreju getInstance() {
		return DEFAULT_INSTANCE;
	}
	
	/**
	 * Get a Arastreju instance for a given profile.
	 * For future use!
	 * @param profile The name of the Arastreju profile.
	 * @return the instance
	 */
	public static Arastreju getInstance(final String profile) {
		return DEFAULT_INSTANCE;
	}

	// -----------------------------------------------------

	public ArastrejuGate login(final String username, final String credential) {
		return defaultFactory.create(null);
	}

	/**
	 * Obtain the root context. Use Carefully! No login will be performed but
	 * the ArastrejuGate will be used in root context.
	 * 
	 * Could be unsupported by specific provider.
	 * 
	 * @return The ArastrejuGate for the root context.
	 */
	public ArastrejuGate rootContext() {
		return defaultFactory.create(null);
	}

	// -----------------------------------------------------

	/**
	 * Private constructor.
	 */
	private Arastreju() {
		// TODO: read initialization from file META-INF/arastreju.profile
		this.defaultFactoryClass = "org.arastreju.bindings.neo4j.Neo4jGateFactory";
		try {
			this.defaultFactory = (ArastrejuGateFactory) Class.forName(
					defaultFactoryClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
