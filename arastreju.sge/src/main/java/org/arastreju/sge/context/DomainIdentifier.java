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
package org.arastreju.sge.context;

import org.arastreju.sge.naming.Namespace;

/**
 * <p>
 *  Identifies a domain of an Arastreju instance.
 * </p>
 *
 * <p>
 * 	Created Jan 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class DomainIdentifier {
	
	public static final String MASTER_DOMAIN = "root";

    // ----------------------------------------------------

	private String domainName;

    private SimpleContextID context;

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 */
	public DomainIdentifier(final String domainName) {
	    this.domainName = domainName;
        this.context = new SimpleContextID(Namespace.LOCAL_CONTEXTS, domainName);
    }
	
	// -----------------------------------------------------

	/**
	 * @return the domain name.
	 */
	public String getDomainName() {
		return domainName;
	}

    /**
     * Get the name of the storage.
     * @return The storage.
     */
    public abstract String getStorage();

    /**
     * Get the initial context.
     * @return The initial context.
     */
    public Context getInitialContext() {
        return context;
    }



	
}
