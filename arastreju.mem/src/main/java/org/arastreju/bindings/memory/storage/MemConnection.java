package org.arastreju.bindings.memory.storage;

import org.arastreju.sge.index.IndexProvider;
import org.arastreju.sge.spi.GraphDataStore;
import org.arastreju.sge.spi.abstracts.AbstractGraphDataConnection;

/**
 * <p>
 *  In-memory specific connection.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemConnection extends AbstractGraphDataConnection {

    public MemConnection(GraphDataStore store, IndexProvider indexProvider) {
        super(store, indexProvider);
    }

}
