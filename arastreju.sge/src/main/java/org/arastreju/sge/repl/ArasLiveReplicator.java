/*
 *  Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.repl;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import org.arastreju.sge.model.*;
import org.arastreju.sge.model.nodes.*;

/**
 * <p>
 *  Socket-based transaction replicator.
 *  
 *  The dispatcher part hooks into transactions and transmits
 *    string representations of the performed semantic graph
 *    operations to another instance of this very class (but
 *    possibly of a different backend-type (and likely on a
 *    different host).
 *    
 *  The receiver part reverses that process, constructing 
 *    Statements or ResourceIDs, for relation or node operations,
 *    respectively.  The abstract methods onNodeOp() and onRelOp()
 *    are then called, which are responsible for performing
 *    the operation on the backing data store, whichever that is.
 *
 *  The dispatcher maintains a sequence number, which increments
 *    whenever a new transaction i.e. a bunch of related GraphOps
 *    begins.  This allows the receiver to recover that 'grouping'
 *    information.  This isn't used yet, however i do feel it will be.
 *    Additionally, for ease of parsing and processing, every 
 *    transaction is terminated by an EOT a.k.a. End Of Transaction message.
 *
 *  This class, or rather subclasses of it, is to be instantiated
 *    in the backend-specific subclass of TxProvider, by overriding
 *    TxProvider.createReplicator().  If this is not done, no
 *    replication facilities are available.
 *    
 *  TODO: -robustness, dispatcher should reconnect on disconnect, 
 *           receiver is to relisten/accept
 *        -host and port settings are hardcoded for now
 * </p>
 *
 * Created: 16.11.2012
 *
 * @author Timo Buhrmester
 */
public abstract class ArasLiveReplicator {
	private List<GraphOp> replLog;
	private Dispatcher dispatcher;
	private int txSeq = 0;

	// -- CONSTRUCTOR -------------------------------------

	public ArasLiveReplicator() {
		replLog = new LinkedList<GraphOp>();
	}

	// -- General interface -------------------------------

	/**
	 * should only be called once
	 * @param lstAddr the address/interface to listen() on. null == "any"
	 * @param lstPort the port to listen() on. [1,65535]
	 * @param rcvHost address or hostname the receiver connect()s against
	 * @param rcvPort port used in said connect()
	 */
	public void init(String lstAddr, int lstPort, String rcvHost, int rcvPort) {
		new Thread(new Receiver(lstAddr, lstPort)).start();
		new Thread(dispatcher = new Dispatcher(rcvHost, rcvPort)).start();
	}
	
	// -- Dispatcher interface ----------------------------

	/**
	 *  Purge replication log (i.e. a transaction is over).
	 *  If the transaction succeeded, dispatch() will usually
	 *  be called before.
	 */
	public void reset() {
		replLog.clear();
	}

	/**
	 * Turn the list of collected graph operations into a
	 * sequence of their textual representations, and queue
	 * everything for actual dispatching 
	 */
	public void dispatch() {
		for (GraphOp g : replLog) {
			dispatcher.queue(txSeq + " " + g.toProtocolString());
		}
		dispatcher.queue((txSeq++) + " EOT"); //end of transaction marker
	}

	/**
	 * Indicate a relation/statement has been added or removed.
	 * Call from TxAction::execute().
	 * @param add true if to be added, false if to be removed
	 * @param stmt the statement in question
	 */
	public void queueRelOp(boolean add, Statement stmt) {
		replLog.add(new RelOp(add, stmt));
	}

	/**
	 * Indicate a node has been added or removed.
	 * Call from TxAction::execute().
	 * @param add true if to be added, false if to be removed
	 * @param node the node in question
	 */
	public void queueNodeOp(boolean add, ResourceID node) {
		replLog.add(new NodeOp(add, node));
	}

	// -- Receiver interface ----------------------------
	
	/** 
	 * called whenever the receiver receives a relation operation, i.e.
	 *   an operation which adds or removes a statement (as opposed to
	 *   a single resource).
	 * @param added false if the statement is to be removed
	 * @param stmt the statement in question
	 */
	protected abstract void onRelOp(boolean added, Statement stmt);
	
	/**
	 * called whenever the receiver receives a node operation, i.e.
	 *   an operation which adds or removes a node (as opppsed to a statement)
	 * @param added false if the node is to be removed
	 * @param node the node in question
	 */
	protected abstract void onNodeOp(boolean added, ResourceID node);
	
	
	// -- INNER CLASSES -----------------------------------

	/**
	 * <p>
	 *  Maintains a socket connection to the receiving part,
	 *  single task is to transmit whatever appears on our sendQ
	 *  See doc of parent for more information
	 * </p>
	 *
	 * Created: 16.11.2012
	 *
	 * @author Timo Buhrmester
	 */
	private class Dispatcher implements Runnable {
		private final List<String> sendQ = new LinkedList<String>();

		private final String rcvHost;
		private final int rcvPort;

		// ------------------------------------------------

		Dispatcher(String receiverHost, int receiverPort) {
			rcvHost = receiverHost;
			rcvPort = receiverPort;
		}

		// ------------------------------------------------

		void queue(String s) {
			synchronized (sendQ) {
				sendQ.add(s);
				sendQ.notifyAll();
			}
		}

		@Override
		public void run() {
			BufferedWriter w;
			Socket s = null;
			do {
				try {
					s = new Socket(rcvHost, rcvPort);
				} catch (IOException e) {
					e.printStackTrace();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			} while (s == null);

			try {
				s.shutdownInput();
				w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				for (;;) {
					String item;
					synchronized (sendQ) {
						while (sendQ.isEmpty()) {
							try {
								sendQ.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
								s.close();
								return; // thread dies when interrupted
							}
						}
						item = sendQ.remove(0);
					}
					w.write(item + "\n");
					w.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * <p>
	 * Accepts a socket, reads and parses whatever the peer side's dispatcher
	 * sent.  Eventually calls upstream for the data to be fed into the data store
	 * See doc of parent for more information
	 * </p>
	 *
	 * Created: 24.11.2012
	 *
	 * @author Timo Buhrmester
	 */
	private class Receiver implements Runnable {
		private final String lstAddr;
		private final int lstPort;

		// ------------------------------------------------

		/**
		 * 
		 * @param listenAddr the interface to listen() on, may be null to
		 *   listen on all available interfaces (a.k.a. INADDR_ANY (most 
		 *   usually equivalent to "0.0.0.0").
		 * @param receiverPort the port to listen() on, >= 1
		 */
		Receiver(String listenAddr, int listenPort) {
			lstAddr = listenAddr;
			lstPort = listenPort;
		}

		// ------------------------------------------------

		private void process(String line) {
			Matcher m = MatcherProvider.matcher(line);

			if (!m.matches()) {
				return; //XXX warn
			}

			/* pattern is:
			 * 		^([0-9]+) (EOT|(?:([+-])(N ([^ ]+)|S ([^ ]+) ([^ ]+) (R ([^ ]+)|V ([^ ]+) ([^ ]+)))))$
			 *
			 * node operation: e.g. "1337 +N http://so.me/node"
			 * 	group 1: sequence number (1337)
			 *  group 3: [+-] (+)
			 *  group 5: URI (http://so.me/node)
			 * 
			 * rel operation with value object:  e.g.  "1337 -S http://so.me/subj http://the.pred/icate V STRING myfancystring"
			 * 	group 1: sequence number (1337)
			 *  group 3: [+-] (-)
			 * 	group 6: subject (http://so.me/subj)
			 *  group 7: predicate (http://the.pred/icate)
			 *  group 10: type (STRING)
			 *  group 11: value (myfancystring)
			 * 
			 * rel operation with resource object: e.g. "1337 -S http://so.me/subj http://the.pred/icate R http://so.me/resource
			 * 	group 1: sequence number (1337)
			 *  group 3: [+-] (-)
			 * 	group 6: subject (http://so.me/subj)
			 *  group 7: predicate (http://the.pred/icate)
			 *  group 9: value (http://so.me/resource)
			 */

			@SuppressWarnings("unused")
			int txSeq = Integer.parseInt(m.group(1));
			if (m.group(2).equals("EOT")) {
				return; // not used ATM
			}

			boolean added = m.group(3).equals("+");

			if (m.group(4).charAt(0) == 'N') {
				onNodeOp(added, new SimpleResourceID(m.group(5)));
				//onNodeOp(added, new SNResource(new QualifiedName(m.group(5)))); //XXX which one of these two?
			} else { // m.group(4).charAt(0) == 'S', since regex matched
				ResourceID sub = new SimpleResourceID(m.group(6));
				ResourceID pred = new SimpleResourceID(m.group(7));
				SemanticNode obj;

				if (m.group(8).charAt(0) == 'R') {
					obj = new SimpleResourceID(m.group(9));
				} else { // m.group(8).charAt(0) == 'V'
					/* the object is converted from a string representation by this constructor */
					obj = new SNValue(ElementaryDataType.valueOf(m.group(10)), m.group(11));
				}

				onRelOp(added, new DetachedStatement(sub, pred, obj)); //what about contexts? XXX
			}
		}

		@Override
		public void run() {
			BufferedReader r;
			try {
				/* this listens on all interfaces, should
				 * be turned into a configuration setting */

				ServerSocket s = new ServerSocket(lstPort, 0, InetAddress.getByName(lstAddr));

				Socket sck = s.accept();
				sck.shutdownOutput();
				r = new BufferedReader(new InputStreamReader(sck.getInputStream()));

				for (;;) {
					String line = r.readLine();
					if (line == null) { /* at EOF */
						return; // for now. TODO: make it relisten
					}
					process(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

/*
 * The sole purpose of this class is to hold a compiled regex to parse the
 * replication protocol, and hand out matchers to anyone who bothers.
 * 
 * The sole reason for this to be a separate class at all, is java's disapproval
 * of final static fields in inner classes (like Receiver, where it belongs)
 * which aren't initialized from a constant expression (like 'pat' below isn't) 
 */
class MatcherProvider {
	static final String PATTERN_STMT_RESOBJ = "R ([^ ]+)"; //R <obj uri>
	static final String PATTERN_STMT_VALOBJ = "V ([^ ]+) ([^ ]+)"; //V <type> <value>
	static final String PATTERN_STMT = "S ([^ ]+) ([^ ]+) (" + PATTERN_STMT_RESOBJ + "|" + PATTERN_STMT_VALOBJ + ")"; //S <sub> <pred> (resobj|valobj)
	static final String PATTERN_NODE = "N ([^ ]+)"; //N <node uri>

	static final Pattern pat = Pattern.compile("^([0-9]+) (EOT|(?:([+-])(" + PATTERN_NODE + "|" + PATTERN_STMT + ")))$");

	static Matcher matcher(String line) {
		return pat.matcher(line);
	}
}
