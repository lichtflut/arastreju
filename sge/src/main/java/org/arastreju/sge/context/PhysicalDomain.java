/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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


/**
 * <p>
 *  Direct identifier of a store based on the store's name.
 * </p>
 *
 * <p>
 * Created ${Date}
 * </p>
 *
 * @author Oliver Tigges
 */
public class PhysicalDomain extends DomainIdentifier {

	/**
	 * Constructor.
	 * @param domainName The domain name.
	 */
	public PhysicalDomain(final String domainName) {
		super(domainName);
	}

	// ----------------------------------------------------

	@Override
	public String getStorage() {
		return getDomainName();
	}

}