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
package org.arastreju.sge.model;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.associations.StatementMetaInfo;
import org.arastreju.sge.model.nodes.SemanticNode;

import java.io.Serializable;

/**
 * <p>
 *  Representation of a statement consisting of subject, predicate and object.
 *  Each statement exists in one or more contexts.
 * </p>
 *
 * <p>
 * 	Created May 5, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Statement extends Serializable {

	ResourceID getSubject(); 
	
	ResourceID getPredicate();
	
	SemanticNode getObject();

	Context[] getContexts();
	
	StatementMetaInfo getMetaInfo();

}
