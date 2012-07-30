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
import org.arastreju.sge.io.StatementContainer;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;


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

    StatementContainer getStatements(Context... ctx);

}
