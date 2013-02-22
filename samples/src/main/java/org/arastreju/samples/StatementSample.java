/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * The Arastreju-Neo4j binding is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
