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
package org.arastreju.sge.model;


import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.SemanticNode;


/**
 * <p>
 *  A detached Statement. This means the statement needs not to be added to the subject's associations.
 * </p>
 *
 * <p>
 * 	Created May 5, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class DetachedStatement extends AbstractStatement {
	
	/**
	 * Creates a new Statement.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @param object The object.
	 * @param contexts The contexts of this statement.
	 */
	public DetachedStatement(final ResourceID subject, final ResourceID predicate,
			final SemanticNode object, final Context... contexts) {
		super(subject, predicate, object, contexts);
	}

}
