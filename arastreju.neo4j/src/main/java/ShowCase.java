/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Simple show case for new use of Arastreju with Neo4j.
 * </p>
 *
 * <p>
 * 	Created Jan 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class ShowCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArastrejuGate gate = Arastreju.getInstance().rootContext();
		
		ModelingConversation mc = gate.startConversation();
		
		QualifiedName qn = new QualifiedName("http://lf.de#Person");
		
		SNClass personClass = new SNResource(qn).asClass();
		
		SNEntity raphi = personClass.createInstance(new QualifiedName("http://lf.de/peaople#Raphi"));
		
		SNEntity ravi = personClass.createInstance(new QualifiedName("http://lf.de/peaople#Ravi"));
		
		mc.attach(raphi);
		mc.attach(ravi);
		

	}

}
