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

import java.util.Collection;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.security.Domain;


/**
 * <p>
 *  Organizer for Contexts und Namespaces.
 * </p>
 *
 * <p>
 * 	Created Sep 1, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Organizer {
	
	Collection<Namespace> getNamespaces();
	
	Namespace registerNamespace(String uri, String prefix);
	
	// ----------------------------------------------------
	
	Collection<Context> getContexts();
	
	Context registerContext(QualifiedName qn);

	// ----------------------------------------------------
	
	/**
	 * Get the master domain or null if not initialized.
	 * @return The master domain or null.
	 */
	Domain getMasterDomain();
	
	/**
	 * Get all registered domains.
	 * @return The domains.
	 */
	Collection<Domain> getDomains();
	
	/**
	 * Initialize and set this domain. 
	 * @param name The unique domain name.
	 */
	Domain initMasterDomain(String name);
	
	/**
	 * Register a new domain, known by this domain.
	 * @param name The unique name.
	 * @param title The title.
	 * @param description A short description.
	 * @return The domain.
	 */
	Domain registerDomain(String name, String title, String description);

	/**
	 * Update info for given domain.
	 * @param domain The domain info.
	 */
	void updateDomain(Domain domain);

	
	
}
