package org.arastreju.sge.spi;

import org.arastreju.sge.persistence.TxProvider;

/**
 * <p>
 *  The connection to a graph data store in the scope of a conversation.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface GraphDataConnection {

    GraphDataStore getStore();

    TxProvider getTxProvider();

}
