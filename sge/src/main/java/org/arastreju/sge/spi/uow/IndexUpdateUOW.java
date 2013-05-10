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
package org.arastreju.sge.spi.uow;

import org.arastreju.sge.index.IndexUpdator;
import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.spi.AssociationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  Listens for new and deleted statements during a transaction. When the transaction is done
 *  the index will be updated.
 * </p>
 *
 * <p>
 *  Created Feb. 14, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class IndexUpdateUOW extends AbstractUnitOfWork implements AssociationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexUpdateUOW.class);

    private final List<Inferencer> inferencers = new ArrayList<Inferencer>();

    private final IndexUpdator indexUpdator;

    // ----------------------------------------------------

    public IndexUpdateUOW(IndexUpdator indexUpdator) {
        this.indexUpdator = indexUpdator;
    }

    // ----------------------------------------------------

    public IndexUpdateUOW add(Inferencer... inferencer) {
        Collections.addAll(inferencers, inferencer);
        return this;
    }

    // ----------------------------------------------------

    @Override
    public void onCreate(Statement stmt) {
        update(stmt.getSubject());

    }

    @Override
    public void onRemove(Statement stmt) {
        update(stmt.getSubject());
    }

    // -- Tx Listener ---------------------------------------

    @Override
    public void onBeforeCommit() {
        LOGGER.debug("On Before Commit.");
        indexUpdator.adviseCommit();
    }

    @Override
    public void onAfterCommit() {
        LOGGER.debug("On After Commit.");
    }

    @Override
    public void onRollback() {
        LOGGER.debug("On Rollback");
    }

    // ----------------------------------------------------

    private void update(ResourceID rid) {
        ResourceNode node = rid.asResource();
        if (!node.isAttached()) {
            throw new IllegalStateException("Didn't expect a non attached node here: " + node);
        }
        indexUpdator.index(node);
    }
}
