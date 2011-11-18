/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
import java.util.List;

import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.query.FieldParam;
import org.arastreju.sge.query.Query;

import de.lichtflut.infra.logging.StopWatch;

/**
 * <p>
 *  Test case for multithreading.
 * </p>
 *
 * <p>
 * 	Created Jun 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class MultiThreadingTest {
	
	/**
	 * Constructor.
	 */
	public MultiThreadingTest(final int numberOfThreads) {
		
		for(int i = 0; i < numberOfThreads; i++) {
			final Thread t = new Thread(new Worker(Arastreju.getInstance().rootContext()));
			t.start();
			System.out.println("Startet Thread: " + t.getId());
		}
	}
	
	// -----------------------------------------------------
	
	public static void main(String[] args) {
		new MultiThreadingTest(10);
	}
	
	// -----------------------------------------------------
	
	static class Worker implements Runnable {
		
		private final ArastrejuGate gate;

		public Worker(final ArastrejuGate gate) {
			this.gate = gate;
		}
		
		public void run() {
			final StopWatch sw = new StopWatch();
			final ModelingConversation mc = gate.startConversation();
			
			final TransactionControl txc = mc.beginTransaction();
			
			final SNClass clazz = createClass();
			mc.attach(clazz);
			
			for (int i = 1; i <= (1000); i++) {
				mc.attach(clazz.createInstance());
			}
			
			sw.displayTime("created instances of " + clazz);
			
			final Query query = gate.createQueryManager().buildQuery().add(new FieldParam(RDF.TYPE, SNOPS.uri(clazz)));
			final List<ResourceNode> instances = query.getResult().toList();
			sw.displayTime("found "+ instances.size() + " instances of " + clazz);
			
			txc.commit();
			
			System.out.println("Thread '" + Thread.currentThread().getId() + "' finished.");
		}
		
		public SNClass createClass() {
			SNClass clazz = new SNClass();
			return clazz;
		}
		
	}
	

}
