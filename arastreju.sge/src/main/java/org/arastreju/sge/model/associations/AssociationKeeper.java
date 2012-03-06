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
package org.arastreju.sge.model.associations;

import java.util.Set;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 * 	Generalized interface for keeping a {@link ResourceNode}'s associations.
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
	 * @return The resolved association set.
	 */
	Set<Statement> getAssociations();
	
	/**
	 * Add an association.
	 * @param association The association.
	 */
	void addAssociation(Statement association);

	/**
	 * Remove an association.
	 * @param association The association.
	 */
	boolean removeAssociation(Statement stmt);
	
	// ----------------------------------------------------
	
	/**
	 * @return The Associations to be removed on attachment.
	 */
	Set<Statement> getAssociationsForRemoval();
	
	/**
	 * Check if this keeper is attached or detached.
	 * @return true when the keeper is detached.
	 */
	boolean isAttached();

}