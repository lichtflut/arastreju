/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.spi.GateLifecycleListener;
import org.arastreju.sge.spi.ProfileCloseListener;

/**
 * <p>
 *  Profile for Arastreju configuration.
 * </p>
 * 
 * <p>
 * 	New profiles can be created either using the public constructors or
 *  by one of the <code>read(...)</code> methods.
 * </p>
 *
 * <p>
 * 	Created May 9, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ArastrejuProfile implements GateLifecycleListener {
	
	public static final String GATE_FACTORY = "org.arastreju.gate-factory";
	
	public static final String ARAS_STORE_DIRECTORY = "org.arastreju.store.directory";
	
	public static final String FILE_SUFFIX = ".profile";
	
	public static final String STANDARD_DIR = "META-INF";
	
	public static final String STANDARD_APP_PROFILE = "arastreju.profile";
	
	public static final String DEFAULT_PROFILE = "arastreju.default.profile";
	
	// -----------------------------------------------------
	
	private final Properties properties = new Properties();
	
	private final Map<String, Object> profileObjects = new HashMap<String, Object>();
	
	private final Set<ArastrejuGate> openGates = new HashSet<ArastrejuGate>();
	
	private final List<ProfileCloseListener> listeners = new ArrayList<ProfileCloseListener>();
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param properties The properties for this profile.
	 */
	public ArastrejuProfile(final Properties properties) {
		this.properties.putAll(properties);
	}
	
	/**
	 * Default constructor. 
	 */
	public ArastrejuProfile() {
	}
	
	/**
	 * Constructor.
	 * @param properties The properties for this profile.
	 */
	protected ArastrejuProfile(final InputStream propertyStream) {
		try {
			properties.load(propertyStream);
			propertyStream.close();
		} catch (IOException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.INITIALIZATION_EXCEPTION, 
					"Could not load profile", e);
		}
	}
	
	// -- READ PROFILE ------------------------------------
	
	public static ArastrejuProfile read() {
		InputStream in = find(STANDARD_APP_PROFILE);
		if (in == null) {
			in = find(DEFAULT_PROFILE);
		}
		if (in == null) {
			throw new ArastrejuRuntimeException(ErrorCodes.INITIALIZATION_EXCEPTION, 
					"Didn't find Arastreu profile file");
		}
		return new ArastrejuProfile(in);
	}
	
	public static ArastrejuProfile read(final String profile) {
		InputStream in = find(profile);
		if (in == null) {
			throw new ArastrejuRuntimeException(ErrorCodes.INITIALIZATION_EXCEPTION, 
					"Didn't find Arastreu profile file: " + profile);
		}
		return new ArastrejuProfile(in);
	}
	
	private static InputStream find(final String profile) {
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();

		String name = profile.trim();
		InputStream in = cl.getResourceAsStream(profile);

		if (in == null && !profile.contains("/")) {
			in = cl.getResourceAsStream(STANDARD_DIR + "/" + name);
		}
		if (in == null && !profile.endsWith(FILE_SUFFIX)) {
			name = name + FILE_SUFFIX;
		}
		if (in == null && !profile.contains("/")) {
			in = cl.getResourceAsStream(STANDARD_DIR + "/" + name);
		}
		return in;
	}
	
	// -- PROPERTIES --------------------------------------
	
	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Checks if a key is defined in the profile.
	 * @param key The key.
	 * @return true if a value for the key is defined.
	 */
	public boolean isPropertyDefined(final String key) {
		return properties.containsKey(key);
	}
	
	/**
	 * Get a single property.
 	 * @param key The key.
	 * @return The value.
	 */
	public String getProperty(final String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * Add all properties.
	 * @param props The properties to be added.
	 * @return This.
	 */
	public ArastrejuProfile addProperties(final Properties props) {
		this.properties.putAll(props);
		return this;
	}
	
	/**
	 * Set a single property.
	 * @param key The key.
	 * @param value The value.
 	 * @return This.
	 */
	public ArastrejuProfile setProperty(final String key, final String value) {
		properties.setProperty(key, value);
		return this;
	}
	
	// -- PROFILE OBJECTS ----------------------------------
	
	/**
	 * @return the properties
	 */
	public Object getProfileObject(final String key) {
		return profileObjects.get(key);
	}
	
	/**
	 * Set a single property.
	 * @param key The key.
	 * @param value The value.
 	 * @return This.
	 */
	public ArastrejuProfile setProfileObject(final String key, final Object value) {
		profileObjects.put(key, value);
		return this;
	}
	
	// -- PROFILE LIFECYCLE --------------------------------
	
	/**
	 * Add a listener for this profile.
	 * @param listener The listener.
	 * @return This.
	 */
	public ArastrejuProfile addListener(final ProfileCloseListener listener) {
		this.listeners.add(listener);
		return this;
	}
	
	public void close() {
		for (ArastrejuGate gate : openGates) {
			gate.close();
		}
		for (ProfileCloseListener listener : listeners) {
			listener.onClosed(this);
		}
	}
	
	// -- GATE LIFECYCLE -----------------------------------
	

	/* (non-Javadoc)
	 * @see org.arastreju.sge.spi.GateLifecycleListener#gateOpened(org.arastreju.sge.ArastrejuGate)
	 */
	public void onOpen(final ArastrejuGate gate) {
		openGates.add(gate);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.spi.GateLifecycleListener#gateClosed(org.arastreju.sge.ArastrejuGate)
	 */
	public void onClose(final ArastrejuGate gate) {
		if (!openGates.contains(gate)) {
			throw new IllegalStateException("Gate is not known by this profile.");
		}
		openGates.remove(gate);
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return properties.toString();
	}


}
