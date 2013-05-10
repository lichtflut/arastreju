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
package org.arastreju.sge.persistence;

import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.PhysicalNodeID;

import java.io.IOException;

/**
 * <p>
 *  Key table mapping qualified names to physical node IDs.
 * </p>
 *
 * <p>
 *  Created Jan 27, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public interface NodeKeyTable<T extends PhysicalNodeID> {

    /**
     * Lookup the physical ID for a qualified name.
     * @param qualifiedName The qualified name.
     * @return The corresponding ID.
     */
    T lookup(QualifiedName qualifiedName);

    /**
     * Add new record of qualified name and according physical ID to table.
     * @param qn The qualified name.
     * @param physicalID The physical ID.
     */
    void put(QualifiedName qn, T physicalID);

    /**
     * Remove qualified name from table.
     * @param qn The qualified name to remove.
     */
    void remove(QualifiedName qn);

    // ----------------------------------------------------

    /**
     * Shutdown the table and potentially release all resources.
     * @throws IOException When shutdown fails.
     */
    void shutdown() throws IOException;

}
