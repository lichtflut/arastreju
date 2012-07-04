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
package org.arastreju.sge.spi;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.config.StoreIdentifier;
import org.arastreju.sge.context.Context;

/**
 * <p>
 *  Context for initialization of a new {@link ArastrejuGate}.
 * </p>
 *
 * <p>
 * 	Created Jan 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class GateContext {
	
	public static final Context[] NO_CTX = new Context[0];
	
	public static final String MASTER_DOMAIN = "root";

    // ----------------------------------------------------

	private final ArastrejuProfile profile;

    private final StoreIdentifier storeIdentifier;
	
	private String domain = MASTER_DOMAIN;
	
	private Context writeContext;
	
	private Context[] readContexts;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 */
	public GateContext(final ArastrejuProfile profile, final StoreIdentifier storeIdentifier) {
		this.profile = profile;
        this.storeIdentifier = storeIdentifier;
    }
	
	// -----------------------------------------------------


    public StoreIdentifier getStoreIdentifier() {
        return storeIdentifier;
    }

    /**
	 * @return the profile
	 */
	public ArastrejuProfile getProfile() {
		return profile;
	}
	
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	
	/**
	 * @return the readContexts
	 */
	public Context[] getReadContexts() {
		if (readContexts == null) {
			return NO_CTX;
		}
		return readContexts;
	}
	
	/**
	 * @return the writeContext
	 */
	public Context getWriteContext() {
		return writeContext;
	}
	
	/**
	 * Check if this context's domain is the master domain.
	 * @return true if this is the master domain.
	 */
	public boolean isMasterDomain() {
		return GateContext.MASTER_DOMAIN.equals(domain);
	}
	
	// ----------------------------------------------------
	
	/**
	 * @param domain the domain to set
	 */
	protected GateContext setDomain(String domain) {
		this.domain = domain;
		return this;
	}
	
}
