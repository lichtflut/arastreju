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
