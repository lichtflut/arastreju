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
package org.arastreju.samples;

import static org.arastreju.sge.SNOPS.associate;
import static org.arastreju.sge.SNOPS.id;
import static org.arastreju.sge.SNOPS.resource;

import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Simple sample to demonstrate working on a detached graph.
 * </p>
 *
 * <p>
 *  Created Mar 08, 2013
 * </p>
 *
 * @author Timo Buhrmester
 */
public class DetachSample {
    private QualifiedName RESOURCE_URI_ALICE = new QualifiedName("http://example.org/alice");
    private QualifiedName RESOURCE_URI_BOB = new QualifiedName("http://example.org/bob");
    private QualifiedName RESOURCE_URI_EVE = new QualifiedName("http://example.org/eve");
    private QualifiedName RESOURCE_URI_KNOWS = new QualifiedName("http://example.org/knows");

    private final Arastreju aras;
    private final ArastrejuGate gate;
    private final Conversation conv;

    public DetachSample() {
        aras = Arastreju.getInstance();
        gate = aras.openMasterGate();
        conv = gate.startConversation();
    }

    public void sample() {
        /* the node is initially detached, statements added
         * to it will /not/ be written into the backend data store */
        ResourceNode subject = resource(id(RESOURCE_URI_ALICE));

        /* create a first statement on the subject
         * the data store won't be affected by this because subject is detached */
        associate(subject, id(RESOURCE_URI_KNOWS), id(RESOURCE_URI_BOB));

        /* attach the node, attaching all of its statements and future ones,
         * statements will be made persistant in the backend data store */
        conv.attach(subject);

        /* this statement is created on a attached node */
        Statement knowEve = associate(subject, id(RESOURCE_URI_KNOWS), id(RESOURCE_URI_EVE));

        /* detach the node and remove a statement
         * the data store won't be affected by this because subject is detached */
        conv.detach(subject);
        conv.removeStatement(knowEve);
    }

    private void tearDown() {
        conv.close();
        gate.close();
    }

    public static void main(String[] args) {
        DetachSample smp = new DetachSample();
        smp.sample();
        smp.tearDown();
    }
}
