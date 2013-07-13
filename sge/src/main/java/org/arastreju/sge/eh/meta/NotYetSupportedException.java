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
package org.arastreju.sge.eh.meta;

/**
 * <p>
 *  Exception thrown on not yet supported features.
 * </p>
 * <p>
 *  Created July 13, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class NotYetSupportedException extends RuntimeException {

    public NotYetSupportedException() {
    }

    public NotYetSupportedException(String message) {
        super(message);
    }

    public NotYetSupportedException(Object obj) {
        super("Not yet supported: " + obj);
    }
}
