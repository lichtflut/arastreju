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
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNScalar;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Simple sample to demonstrate attaching and detaching a statement.
 * </p>
 *
 * <p>
 *  Created Mar 15, 2013
 * </p>
 *
 * @author Timo Buhrmester
 */

public class ContextSample {
    private QualifiedName RESOURCE_URI_ALICE = QualifiedName.fromURI("http://example.org/alice");
    private QualifiedName RESOURCE_URI_AGE = QualifiedName.fromURI("http://example.org/isofage");

    private QualifiedName CONTEXT_URI_ALICE = QualifiedName.fromURI("http://example.org/alicectx");
    private QualifiedName CONTEXT_URI_BOB = QualifiedName.fromURI("http://example.org/bobctx");
    private QualifiedName CONTEXT_URI_DAVE = QualifiedName.fromURI("http://example.org/davectx");

    private final Arastreju aras;
    private final ArastrejuGate gate;

    public ContextSample() {
        aras = Arastreju.getInstance();
        gate = aras.openMasterGate();
    }

    /*
     * simple context model:
     *  Alice (44) pretends to be 25 to Dave, however Bob does know her real age.
     */
    public void sample() {
        /* create a context for everyone */
        Context aliceCtx = new SimpleContextID(CONTEXT_URI_ALICE);
        Context bobCtx = new SimpleContextID(CONTEXT_URI_BOB);
        Context daveCtx = new SimpleContextID(CONTEXT_URI_DAVE);

        /* create a conversation for everyone with their respective context */
        Conversation aliceConv = gate.startConversation(aliceCtx);
        Conversation bobConv = gate.startConversation(bobCtx);
        Conversation daveConv = gate.startConversation(daveCtx);

        /* create statement "alice is of age 44", and add it using
         * alice's and bob's conversations */
        Statement stmt = associate(resource(id(RESOURCE_URI_ALICE)), id(RESOURCE_URI_AGE),
                new SNScalar(44));
        aliceConv.addStatement(stmt);
        bobConv.addStatement(stmt);

        /* create statement "alice is of age 25" and add it using
         * dave's conversation */
        stmt = associate(resource(id(RESOURCE_URI_ALICE)), id(RESOURCE_URI_AGE),
                new SNScalar(25));
        daveConv.addStatement(stmt);

        /* return the ResourceNode for alice from her conversation,
         * then iterate over all statements.  There should be one
         * claiming her age is 44, since she obviously knows the
         * real thing. */
        ResourceNode aliceNode = aliceConv.findResource(RESOURCE_URI_ALICE);
        System.out.println("===Alice's view===");
        for (Statement s : aliceNode.getAssociations()) {
            System.out.println("Statement: " + s);
        }

        /* do the same, but use bob's conversation this time.
         * The result should be the same, because Bob is in to
         * the truth, too. */
        aliceNode = bobConv.findResource(RESOURCE_URI_ALICE);
        System.out.println("===Bob's view===");
        for (Statement s : aliceNode.getAssociations()) {
            System.out.println("Statement: " + s);
        }

        /* Do it once again, now with Dave's conversation.
         * He should think Alice is 25. */
        aliceNode = daveConv.findResource(RESOURCE_URI_ALICE);
        System.out.println("===Dave's view===");
        for (Statement s : aliceNode.getAssociations()) {
            System.out.println("Statement: " + s);
        }

        aliceConv.close();
        bobConv.close();
        daveConv.close();
    }

    private void tearDown() {
        gate.close();
    }

    public static void main(String[] args) {
        ContextSample smp = new ContextSample();
        smp.sample();
        smp.tearDown();
    }
}
