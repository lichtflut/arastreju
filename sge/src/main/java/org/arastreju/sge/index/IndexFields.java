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
package org.arastreju.sge.index;

/**
 * <p>
 *  Definition of fields in the index.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class IndexFields {

    /**
     * Index key representing a resource's qualified name.
     */
    public static final String QUALIFIED_NAME = "qn";

    /**
     * Index key for a value field of a resource.
     */
    public static final String RESOURCE_VALUE = "resource-value";

    /**
     * Index key for a relation field of a resource.
     */
    public static final String RESOURCE_RELATION = "resource-relation";


}
