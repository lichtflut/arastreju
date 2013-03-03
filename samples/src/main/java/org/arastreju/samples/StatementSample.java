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

import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;

import static org.arastreju.sge.SNOPS.*;

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
public class StatementSample {
	private static String RESOURCE_URI_ALICE = "http://example.org/alice";
	private static String RESOURCE_URI_BOB = "http://example.org/bob";
	private static String RESOURCE_URI_KNOWS = "http://example.org/knows";

	private static Arastreju aras;
	private static ArastrejuGate gate;
	private static Conversation conv;

	private static void setUp() {
		aras = Arastreju.getInstance();
		gate = aras.openMasterGate();
		conv = gate.startConversation();
	}

	private static void tearDown() {
		conv.close();
		gate.close();
	}

	public static void main(String[] args) {
		setUp();

		/* create a new statement "alice talks_to bob" */
		ResourceNode subject = resource(id(qualify(RESOURCE_URI_ALICE)));
		ResourceID predicate = id(qualify(RESOURCE_URI_KNOWS));
		ResourceID object = id(qualify(RESOURCE_URI_BOB));

		Statement stmt = associate(subject, predicate, object);

		/* add statement to conversation, persisting it */
		conv.addStatement(stmt);

		/* detach again if you want */
		conv.removeStatement(stmt);

		tearDown();
	}
}
