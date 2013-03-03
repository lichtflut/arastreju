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
	private final QualifiedName RESOURCE_URI = new QualifiedName("http://example.org/my_resource");


	public void sample() {
		/* create instance and open master gate */
		Arastreju aras = Arastreju.getInstance();
		ArastrejuGate gate = aras.openMasterGate();

		/* create conversation for manipulating a graph */
		Conversation conv = gate.startConversation();

		/* create a node */
		ResourceNode node = new SNResource(RESOURCE_URI);

		/* attach node, making it persist in the storage back-end */
		conv.attach(node);

		/* detach the node again */
		conv.detach(node);

		conv.close();
		gate.close();
	}

	public static void main(String[] args) {
		new NodeSample().sample();
	}
}
