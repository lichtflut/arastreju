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
package org.arastreju.sge.spi;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.context.DomainIdentifier;

/**
 * <p>
 *  Factory for creation of implementation specific {@link ArastrejuGate}s.
 * </p>
 *
 * <p>
 * 	Created Jan 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class ArastrejuGateFactory {
	
	private final ArastrejuProfile profile;

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 */
	public ArastrejuGateFactory(final ArastrejuProfile profile) {
		this.profile = profile;
	}
	
	// -----------------------------------------------------

	public abstract ArastrejuGate create(final DomainIdentifier identifier) throws GateInitializationException;
	
	// -----------------------------------------------------+
	
	/**
	 * @return the profile
	 */
	protected ArastrejuProfile getProfile() {
		return profile;
	}
	
}
