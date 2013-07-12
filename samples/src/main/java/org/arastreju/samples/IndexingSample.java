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
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;

/**
 * <p>
 *  Simple sample to demonstrate attaching and detaching a statement.
 * </p>
 *
 * <p>
 * 	Created Feb 22, 2013
 * </p>
 *
 * @author Timo Buhrmester
 */
public class IndexingSample {
    private QualifiedName RESOURCE_URI_ALICE = QualifiedName.from("http://example.org/alice");
    private QualifiedName RESOURCE_URI_BOB = QualifiedName.from("http://example.org/bob");
    private QualifiedName RESOURCE_URI_EVE = QualifiedName.from("http://example.org/eve");
    private QualifiedName RESOURCE_URI_KNOWS = QualifiedName.from("http://example.org/knows");

    private final Arastreju aras;
    private final ArastrejuGate gate;
    private final Conversation conv;

    public IndexingSample() {
        aras = Arastreju.getInstance();
        gate = aras.openMasterGate();
        conv = gate.startConversation();
    }

    public void sample() {
        createAssociation(resource(id(RESOURCE_URI_ALICE)), id(RESOURCE_URI_KNOWS), id(RESOURCE_URI_BOB));

        /* search for everyone who knows bob */
        Query query = conv.createQuery().addField(RESOURCE_URI_KNOWS.toURI(), RESOURCE_URI_BOB.toURI());
        QueryResult result = query.getResult();

        /* in this case we know it's only alice, so we can use .getSingleNode() */
        System.out.println("found in index: " + result.getSingleNode().toURI());

        /* introduce bob to eve, too */
        createAssociation(resource(id(RESOURCE_URI_EVE)), id(RESOURCE_URI_KNOWS), id(RESOURCE_URI_BOB));

        /* search again */
        result = query.getResult();

        /* now we should find two, alice and eve */
        for (ResourceNode node : result) {
            System.out.println("found in index: " + node.toURI());
        }
    }

    private void createAssociation(ResourceNode subject, ResourceID predicate, ResourceID object) {
        Statement stmt = associate(subject, predicate, object);
        conv.addStatement(stmt);
    }

    private void tearDown() {
        conv.close();
        gate.close();
    }

    public static void main(String[] args) {
        IndexingSample smp = new IndexingSample();
        smp.sample();
        smp.tearDown();
    }
}
