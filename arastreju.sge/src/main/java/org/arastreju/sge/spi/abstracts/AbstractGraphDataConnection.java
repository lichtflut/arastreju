package org.arastreju.sge.spi.abstracts;

import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.spi.GraphDataConnection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Abstract base of graph data connections.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractGraphDataConnection<T extends ConversationContext> implements GraphDataConnection {

    private final Set<T> openConversations = new HashSet<T>();

    // ----------------------------------------------------

    public void register(T conversationContext) {
        openConversations.add(conversationContext);
    }

    public void unregister(T conversationContext) {
        openConversations.remove(conversationContext);
    }

    /**
     * (re-)open the connection.
     */
    public void open() {
    }

    /**
     * Close the connection and free all resources.
     */
    public void close() {
        List<T> copy = new ArrayList<T>(openConversations);
        // iterating over copy because original will be remove itself while closing.
        for (T cc : copy) {
            cc.close();
        }
    }

    // ----------------------------------------------------

    protected Set<T> getOpenConversations() {
        return openConversations;
    }
}
