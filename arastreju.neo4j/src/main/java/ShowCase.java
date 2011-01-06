/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
		
		SNEntity raphi = personClass.createInstance(null, new QualifiedName("http://lf.de/peaople#Raphi"));
		
		SNEntity ravi = personClass.createInstance(null, new QualifiedName("http://lf.de/peaople#Ravi"));
		
		mc.attach(raphi);
		mc.attach(ravi);
		

	}

}
