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
package org.arastreju.sge.query;

import java.util.List;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Manager for queries such as SPARQL and for index lookups.
 * </p>
 *
 * <p>
 * 	Created Sep 2, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class QueryManager {

	/**
	 * Find resources with a given tag. 
	 * @param tag The tag.
	 * @return The corresponding result list.
	 */
	public abstract List<ResourceNode> findByTag(String tag);
	
	/**
	 * Find resources with a given tag on a special predicate.
	 * The result list will contain all resource nodes with a relation with this predicate and tag. 
	 * @param predicate The predicate.
	 * @param tag The tag.
	 * @return The corresponding result list.
	 */
	public abstract List<ResourceNode> findByTag(ResourceID predicate, String tag);
	
	/**
	 * Find resources with the given rdf:type.
	 * @param type The resource ID of the type.
	 * @return A list with all resources having given resource type as rdf:type.
	 */
	public abstract List<ResourceNode> findByType(ResourceID type);
	
	/**
	 * Find all incoming associations for the given resource.
	 * @param resource The resource.
	 * @return A list with all associations where the resource is the client/object.
	 */
	public abstract List<Association> findIncomingAssociations(ResourceID resource);
	
}
