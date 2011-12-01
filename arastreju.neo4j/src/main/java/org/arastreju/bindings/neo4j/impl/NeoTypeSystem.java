/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.bindings.neo4j.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.arastreju.sge.TypeSystem;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNClass;

/**
 * <p>
 *  Neo specific implementation of TypeSystem.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoTypeSystem implements TypeSystem {
	
	private final SemanticNetworkAccess store;

	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param store The {@link SemanticNetworkAccess}.
	 */
	public NeoTypeSystem(final SemanticNetworkAccess store) {
		this.store = store;
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.sge.TypeSystem#getAllClasses()
	 */
	public Set<SNClass> getAllClasses() {
		final Set<SNClass> result = new HashSet<SNClass>();
		final List<ResourceNode> nodes = store.getIndex().lookup(RDF.TYPE, RDFS.CLASS);
		for (ResourceNode current : nodes) {
			result.add(current.asClass());
		}
		return result;
	}

}
