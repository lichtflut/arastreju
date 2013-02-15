package org.arastreju.sge.spi;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;

/**
 * <p>
 *  Listener for created and removed statements.
 * </p>
 *
 * <p>
 *  Created Feb. 14, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public interface AssociationListener {

    /**
     * Called when a statement is being created.
     * @param stmt The statement.
     */
    void onCreate(Statement stmt);

    /**
     * Called when a statement is being removed.
     * @param stmt The statement.
     */

    void onRemove(Statement stmt);
}
