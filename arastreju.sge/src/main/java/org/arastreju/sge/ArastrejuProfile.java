/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;

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
public class ArastrejuProfile {
	
	public static final String GATE_FACTORY = "org.arastreju.gate-factory";
	
	public static final String ARAS_STORE_DIRECTORY = "org.arastreju.store.directory";
	
	public static final String FILE_SUFFIX = ".profile";
	
	public static final String STANDARD_DIR = "META-INF";
	
	public static final String STANDARD_APP_PROFILE = "arastreju.profile";
	
	public static final String DEFAULT_PROFILE = "arastreju.default.profile";
	
	private final Properties properties = new Properties();
	
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
	
	// -----------------------------------------------------
	
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
	
	// -----------------------------------------------------
	
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
	public boolean isDefined(final String key) {
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
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return properties.toString();
	}

}
