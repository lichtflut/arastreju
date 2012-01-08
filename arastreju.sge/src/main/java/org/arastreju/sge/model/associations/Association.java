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

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.AbstractStatement;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  A statement attached to it's subject node.
 * </p>
 *
 * <p>
 * 	Created Jan 6, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class Association extends AbstractStatement {

	/**
	 * Creates a new association.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @param object The object.
	 * @param ctx The contexts of this statement.
	 */
	public Association(ResourceNode subject, ResourceID predicate, SemanticNode object, Context... ctx) {
		super(subject, predicate, object, ctx);
		if (!subject.getAssociations().contains(this)) {
			subject.addAssociation(predicate, object, ctx);
		}
	}
	
}
