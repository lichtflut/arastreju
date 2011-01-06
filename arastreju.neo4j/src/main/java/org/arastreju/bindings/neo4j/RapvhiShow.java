/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jan 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class RapvhiShow {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArastrejuGate gate = Arastreju.getInstance().rootContext();
		
		ModelingConversation mc = gate.startConversation();
		
		QualifiedName qn = new QualifiedName("http://lf.de#Person");
		
		SNClass personClass = new SNResource(qn).asClass();
		
		SNEntity raphi = personClass.createInstance(null, new QualifiedName("http://lf.de/peaople#Raphi"));
		
		SNEntity ravi = personClass.createInstance(null, new QualifiedName("http://lf.de/people#Ravi"));
		
		mc.attach(raphi);
		mc.attach(ravi);
		
		mc.detach(raphi);
		mc.detach(ravi);
		
		ResourceNode r2 = mc.findResource(new QualifiedName("http://lf.de/peaople#Raphi"));
		
		if (r2.asEntity().isInstanceOf(personClass)){
			System.out.println("Der ist eine Person: " + r2.getAssociations() );
		}
		
		Association.create(ravi, Aras.HAS_SURNAME, new SNText("Ravi"), null);
		
		mc.attach(ravi);
		
		System.out.println("Alle Resourcen mit name 'Ravi': " + mc.createQueryManager().findByTag("Ravi"));
		

	}

}
