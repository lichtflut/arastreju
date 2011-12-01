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
package org.arastreju.sge.model.associations;

import java.util.Set;

import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 * 	Generalized interface for resolving of a {@link SemanticNode}'s associations.
 * </p>
 *
 * <p>
 * 	Created Oct 11, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface AssociationKeeper {
	
	/**
	 * @return The active Associations.
	 */
	Set<Association> getAssociations();
	
	boolean isAttached();
	
	void add(Association assoc);

	boolean remove(Association assoc);
	
	boolean clearAssociations();

	/**
	 * @return The Associations to be removed on attachment.
	 */
	Set<Association> getAssociationsForRemoval();

}