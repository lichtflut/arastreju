import java.util.List;

import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.persistence.TransactionControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lichtflut.infra.logging.StopWatch;

/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jun 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class MultiThreadingTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MultiThreadingTest.class);
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 */
	public MultiThreadingTest(final int numberOfThreads) {
		
		for(int i = 0; i < numberOfThreads; i++) {
			final Thread t = new Thread(new Worker(Arastreju.getInstance().rootContext()));
			t.start();
			logger.info("Startet Thread: " + t.getId());
		}
	}
	
	// -----------------------------------------------------
	
	public static void main(String[] args) {
		new MultiThreadingTest(1);
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
			
			final TransactionControl txc = mc.getTransactionControl();
			txc.begin();
			
			final SNClass clazz = createClass();
			mc.attach(clazz);
			
			for (int i = 1; i <= (1000); i++) {
				mc.attach(clazz.createInstance());
			}
			
			sw.displayTime("created instances of " + clazz);
			
			final List<ResourceNode> instances = mc.createQueryManager().findByTag(RDF.TYPE, clazz.getQualifiedName().toURI());
			sw.displayTime("found "+ instances.size() + " instances of " + clazz);
			
			txc.commit();
			
			logger.info("Thread '" + Thread.currentThread().getId() + "' finished.");
		}
		
		public SNClass createClass() {
			SNClass clazz = new SNClass();
			return clazz;
		}
		
	}
	

}
