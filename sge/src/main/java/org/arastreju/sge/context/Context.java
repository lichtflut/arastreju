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

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Special ResourceID that represents a context for information.
 * </p>
 *
 * <p>
 * 	Created Sep 1, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Context extends ResourceID, Comparable<Context> {

    String LOCAL_CONTEXTS_NAMESPACE = "local:contexts:";

    QualifiedName DOMAIN_CONTEXT = QualifiedName.from(LOCAL_CONTEXTS_NAMESPACE, "domain");

}
