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

import org.arastreju.bindings.neo4j.Neo4jGateFactory;
import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Simple sample to demonstrate creating and attaching a resource.
 * </p>
 *
 * <p>
 * 	Created Feb 22, 2013
 * </p>
 *
 * @author Timo Buhrmester
 */
public class NodeSample {
	public static void main(String[] args) {
		/* create new default profile, for neo4j backend */
		ArastrejuProfile profile = new ArastrejuProfile("myprofile");
		profile.setProperty(ArastrejuProfile.GATE_FACTORY, Neo4jGateFactory.class.getCanonicalName());

		/* create arastreju instance */
		Arastreju aras = Arastreju.getInstance(profile);

		/* open master gate */
		ArastrejuGate gate = aras.openMasterGate();

		/* start a conversation */
		Conversation conv = gate.startConversation();

		/* create a new resource */
		ResourceNode node = new SNResource(new QualifiedName("http://example.org/my_resource"));

		/* attach node to conversation, persisting it */
		conv.attach(node);

		/* detach the node again */
		conv.detach(node);

		/* clean up */
		conv.close();
		gate.close();
		profile.close();
	}
}
