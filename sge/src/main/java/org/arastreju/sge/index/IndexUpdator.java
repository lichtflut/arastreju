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

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Interface for update, change and delete operations on Arastreju index.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface IndexUpdator {

    /**
     * Index this node with all it's statements, regarding the current primary context.
     * If the node already has been indexed, it will be updated.
     * @param node The node to index.
     */
    void index(ResourceNode node);

    /**
     * Remove the resource identified by the qualified name form the index.
     * @param qn The qualified name.
     */
    void remove(QualifiedName qn);

    // ----------------------------------------------------

    /**
     * Bring queued changes to index.
     */
    void flush();

    /**
     * Advise the index to perform a commit at next suitable condition.
     */
    void adviseCommit();

}