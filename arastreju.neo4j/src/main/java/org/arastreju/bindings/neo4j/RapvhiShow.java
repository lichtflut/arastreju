/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.bindings.neo4j;

import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Just a show case.
 * </p>
 *
 * <p>
 * 	Created Jan 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class RapvhiShow {

	private RapvhiShow() {}
	
	// -----------------------------------------------------
	
	/**
	 * Main method.
	 * @param args Arguments.
	 */
	public static void main(final String[] args) {
		ArastrejuGate gate = Arastreju.getInstance().rootContext();
		
		ModelingConversation mc = gate.startConversation();
		
		QualifiedName qn = new QualifiedName("http://lf.de#Person");
		
		SNClass personClass = new SNResource(qn).asClass();
		
		SNEntity raphi = personClass.createInstance(new QualifiedName("http://lf.de/people#Raphi"));
		SNEntity raphi2 = personClass.createInstance(new QualifiedName("http://lf.de/people#Raphi"));
		
		SNEntity ravi = personClass.createInstance(new QualifiedName("http://lf.de/people#Ravi"));
		
		mc.attach(raphi);
		mc.attach(raphi2);
		mc.attach(ravi);
		
		mc.detach(raphi);
		mc.detach(ravi);
		
		ResourceNode r2 = mc.findResource(new QualifiedName("http://lf.de/people#Raphi"));
		
		if (r2.asEntity().isInstanceOf(personClass)){
			System.out.println("Der ist eine Person: " + r2.getAssociations());
		}
		
		Association.create(ravi, Aras.HAS_SURNAME, new SNText("Ravi"));
		
		mc.attach(ravi);
		
		System.out.println("Alle Resourcen mit name 'Ravi': " + mc.createQueryManager().findByTag("Ravi"));

	}

}
