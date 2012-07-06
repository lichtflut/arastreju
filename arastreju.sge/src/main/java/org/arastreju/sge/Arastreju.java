/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge;

import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.context.MasterDomain;
import org.arastreju.sge.context.PhysicalDomain;
import org.arastreju.sge.context.VirtualDomain;
import org.arastreju.sge.spi.ArastrejuGateFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Properties;

/**
 * <p>
 *  Central API class for obtaining an {@link ArastrejuGate}.
 * </p>
 * 
 * <p>
 *  You obtain an Arastreju instance by one of the <code>getInstance(...)</code> methods, where you
 *  can pass the profile to be used (see {@link ArastrejuProfile}).
 * </p>
 * 
 * <p>
 *  Created Jan 4, 2011
 * </p>
 * 
 * @author Oliver Tigges
 */
public final class Arastreju {

	private final static Arastreju DEFAULT_INSTANCE = new Arastreju();

	// -----------------------------------------------------

    private final ArastrejuGateFactory factory;
	private final ArastrejuProfile profile;
	
	private static HashMap<String, DomainIdentifier> contextMap = new HashMap<String, DomainIdentifier>();

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
	 * A profile describes the binding to the graph store (e.g. Neo4j).
	 * @param profile The name/path of the Arastreju profile.
	 * @return the instance
	 */
	public static Arastreju getInstance(final String profile) {
		return new Arastreju(profile);
	}
	
	/**
	 * Get a Arastreju instance for a given profile.
	 * A profile describes the binding to the graph store (e.g. Neo4j).
	 * @param profile The name/path of the Arastreju profile.
	 * @param properties Additional properties.
	 * @return the instance
	 */
	public static Arastreju getInstance(final String profile, final Properties properties) {
		return getInstance(ArastrejuProfile.read(profile).addProperties(properties));
	}
	
	/**
	 * Get a Arastreju instance for a given profile.
	 * A profile describes the binding to the graph store (e.g. Neo4j).
	 * @param profile An initialized ArastrejuProfile.
	 * @return the instance
	 */
	public static Arastreju getInstance(final ArastrejuProfile profile) {
		return new Arastreju(profile);
	}

	// -----------------------------------------------------

    /**
     * @deprecated  Use openMasterGate() instead.
     */
    @Deprecated
	public ArastrejuGate rootContext() {
		return openMasterGate();
	}

    /**
     * Open the gate to the master domain.
     *
     * <p>
     *  Specific providers can deny root access. Or allow root access only as long
     *  as user 'root' has no credential set.
     * </p>
     *
     * @return The ArastrejuGate for the root context.
     */
    public ArastrejuGate openMasterGate() {
        return openGate(DomainIdentifier.MASTER_DOMAIN);
    }

    /**
     * Open a gate to a domain.
     *
     * <p>
     *  Specific providers can deny root access.
     * </p>
     *
     * @param domain The domain.
     * @return The ArastrejuGate for the root context.
     */
    public ArastrejuGate openGate(String domain) {
        String ctxKey = profile.getName() + "::" + domain;
        DomainIdentifier ctx = contextMap.get(ctxKey);
        if(ctx==null){
            ctx = createDomainIdentifier(domain);
            contextMap.put(ctxKey, ctx);
        }
        return factory.create(ctx);
    }
	
	/**
	 * @deprecated  Use openGate() instead.
	 */
    @Deprecated
	public ArastrejuGate rootContext(String domain) {
       return openGate(domain);
	}

	// -----------------------------------------------------
	
	/**
	 * Create and initialize the Gate Context.
	 */
	private DomainIdentifier createDomainIdentifier(String domain) {
        if (DomainIdentifier.MASTER_DOMAIN.equals(domain)) {
            return new MasterDomain();
        } else if (profile.isPropertyEnabled(ArastrejuProfile.ENABLE_VIRTUAL_DOMAINS)) {
            return new VirtualDomain(domain);
        } else {
            return new PhysicalDomain(domain);
        }
	}
	
	// -- PRIVATE CONSTRUCTORS -----------------------------

	/**
	 * Private constructor.
	 */
	private Arastreju() {
		this(ArastrejuProfile.read());
	}
	
	/**
	 * Private constructor.
	 * @param profile path to the profile file.
	 */
	private Arastreju(final String profile) {
		this(ArastrejuProfile.read(profile));
	}
	
	/**
	 * Private constructor.
	 * @param profile path to the profile file.
	 */
	@SuppressWarnings("rawtypes")
	private Arastreju(final ArastrejuProfile profile) {
		this.profile = profile;
        String factoryClass = profile.getProperty(ArastrejuProfile.GATE_FACTORY);
		try {
			final Constructor constructor = 
					Class.forName(factoryClass).getConstructor(ArastrejuProfile.class);
			this.factory = (ArastrejuGateFactory) constructor.newInstance(profile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
