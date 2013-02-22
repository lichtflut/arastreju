package org.arastreju.sge.spi.uow;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.spi.AssociationListener;
import org.arastreju.sge.spi.GraphDataConnection;
import org.arastreju.sge.spi.WorkingContext;

/**
 * <p>
 *  Listener for statement changes. Notifies other open conversations through connection.
 * </p>
 * <p/>
 * <p>
 *  Created 15.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class OpenConversationNotifier implements AssociationListener {

    private final GraphDataConnection connection;
    private final WorkingContext ctx;

    // ----------------------------------------------------

    public OpenConversationNotifier(GraphDataConnection connection, WorkingContext ctx) {
        this.ctx = ctx;
        this.connection = connection;
    }

    // ----------------------------------------------------

    @Override
    public void onCreate(Statement stmt) {
        notify(stmt);
    }

    @Override
    public void onRemove(Statement stmt) {
        notify(stmt);
    }

    // ----------------------------------------------------

    private void notify(Statement stmt) {
        connection.notifyModification(stmt.getSubject().getQualifiedName(), ctx);
    }


}
