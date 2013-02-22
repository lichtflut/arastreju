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
package org.arastreju.sge.spi.impl;

import org.arastreju.sge.spi.PhysicalNodeID;

/**
 * <p>
 *  Standard implementation of physical node ID, where the ID is a number.
 * </p>
 *
 * <p>
 * Created 22.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class NumericPhysicalNodeID implements PhysicalNodeID {

    private final Number id;

    // ----------------------------------------------------

    public NumericPhysicalNodeID(Number id) {
        this.id = id;
    }

    // ----------------------------------------------------+

    public long asLong() {
        return id.longValue();
    }
}
