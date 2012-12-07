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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 *    information.
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
	private final Logger logger = LoggerFactory.getLogger(ArasLiveReplicator.class);
	
	private List<GraphOp> replLog;
	private Dispatcher dispatcher;
	private int txSeq = 0;

	// -- CONSTRUCTOR -------------------------------------

	public ArasLiveReplicator() {
		replLog = new LinkedList<GraphOp>();
		logger.debug("ArasLiveReplicator()");
	}

	// -- General interface -------------------------------

	/**
	 * should only be called once
	 * @param lstAddr the address/interface to listen() on. null == "any"
	 * @param lstPort the port to listen() on. [1,65535]
	 * @param rcvHost address or hostname the receiver connect()s against
	 * @param rcvPort port used in said connect()
	 */

	/* get in the addresses/ports /somehow/ for testing. see also below and TxProvider
	static LinkedList<int[]> deq = new LinkedList<int[]>();
	public static int[] pop() {
		return deq.removeFirst();
	}
	public static void push(int[] is) {
	    deq.addLast(is);
    }*/

	public void init(String lstAddr, int lstPort, String rcvHost, int rcvPort) {
		logger.debug("init()");
		//int [] p = pop();
		//new Thread(new Receiver(lstAddr, p[0])).start();
		//new Thread(dispatcher = new Dispatcher("127.0.0.1", p[1])).start();
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
			dispatcher.queue(txSeq, g.toProtocolString());
		}
		dispatcher.queue(txSeq++, "EOT"); //end of transaction marker
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
	protected abstract void onRelOp(int txSeq, boolean added, Statement stmt);
	
	/**
	 * called whenever the receiver receives a node operation, i.e.
	 *   an operation which adds or removes a node (as opppsed to a statement)
	 * @param added false if the node is to be removed
	 * @param node the node in question
	 */
	protected abstract void onNodeOp(int txSeq, boolean added, ResourceID node);
	
	/**
	 * called at the end of every transaction
	 * @param txSeq
	 */
	protected abstract void onEndOfTx(int txSeq);
	
	
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
		private final Logger logger = LoggerFactory.getLogger(ArasLiveReplicator.Dispatcher.class);

		private final String rcvHost;
		private final int rcvPort;

		/* the following four members are fiddled with by multiple threads.
		 * txBackup, our main structure used to store transactions in, also 
		 *  functions as the lock to serialize access with.
		 * thus, access to any of these may only happen inside of a
		 *  synchronize(txBackup){}-block*/
		private final Map<Integer, List<String>> txMap = new HashMap<Integer, List<String>>();
		private int minTxSeq = 0; //stores the lowest (a.k.a. earliest) tx we remember
		private int curTxSeq = -1; //stores the highest tx we saw, finished or not
		private boolean curTxDone = false; //stores whether tx# curTxSeq has seen its "EOT" already

		// ------------------------------------------------

		Dispatcher(String receiverHost, int receiverPort) {
			rcvHost = receiverHost;
			rcvPort = receiverPort;
		}

		// ------------------------------------------------

		void queue(int txSeq, String s) {
			synchronized (txMap) {
				List<String> l = txMap.get(txSeq);

				if (l == null)
					txMap.put(txSeq, (l = new LinkedList<String>()));

				l.add(s);

				curTxSeq = txSeq;
				curTxDone = s.equals("EOT");
				
				txMap.notifyAll();
			}

		}

		@Override
		public void run() {
			BufferedWriter w;
			BufferedReader r;
			Socket s;

			int nextTxSeq; //stores the next tx we're going to send

			while (!shutdownRequested()) {
				try {
					s = connectSocket(); //retries on failure, except when interrupted
					logger.debug("connected a socket (to " + rcvHost + ":" + rcvPort + ")");
				} catch (InterruptedException e) {
					logger.warn("interrupted while connecting (to " + rcvHost + ":" + rcvPort + ")");
					continue; //re-evaluate shutdown condition
				}

				try {
					w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					r = new BufferedReader(new InputStreamReader(s.getInputStream()));

					/* receiver tells us the last complete transaction it saw,
					 * or -1 if it didn't see any transaction complete so far.
					 * therefore we know where to resume operation:	 */
					nextTxSeq = Integer.parseInt(r.readLine()) + 1;

					logger.debug("(re)starting replication with tx#" + nextTxSeq);

					while (!shutdownRequested()) {
						boolean shutdown = false;
						List<String> opList = null;

						/* the receiver is free to tell us about his successes,
						 * allowing us to purge the table of transactions we
						 * won't need anymore. */
						if (r.ready()) {
							int good = Integer.parseInt(r.readLine());

							logger.debug("receiver acknowledged tx#" + good);

							synchronized (txMap) {
								/* minTxSeq does have a value >= 0 by now, since 
								 * /something/ must have been queue()d in the past
								 * for the receiver to tell us about that he got it */
								for (int i = minTxSeq; i <= good; i++) {
									txMap.remove(i);
								}
								minTxSeq = good + 1;
							}

						}

						synchronized (txMap) {
							/* there isn't anything to do for us as long as
							 * ``nextTxSeq > curTxSeq'' (meaning we're waiting
							 *  for another tx to show up), or as long as the tx 
							 *  we're about to send isn't fully queue()'d yet.
							 * so we wait until there's work to do.
							 * in case of a requested shutdown we abort anyway.*/
							while (!(shutdown = shutdownRequested())
							        && (nextTxSeq > curTxSeq
							                || (nextTxSeq == curTxSeq && !curTxDone))) {
								try {
									txMap.wait(1000); //arbitrary timeout, 1s
								} catch (InterruptedException e) {
									logger.warn("interrupted while waiting for data");
									continue; //just for clarity
								}
							}

							if (shutdown)
								break;

							/* the other thread won't touch this list
							 * anymore (since the transaction is done)
							 * so it's safe to deal with it outside the lock */
							opList = txMap.get(nextTxSeq);
						}

						for (String line : opList) {
							logger.debug("writing " + line);
							w.write(line + "\n");
						}
						w.flush();
						logger.debug("dispatched tx#" + nextTxSeq);
						nextTxSeq++;
					}
				} catch (IOException e) { //thrown by get{In,Out}putStream(), readLine(), write(), flush()
					e.printStackTrace();
					try {
						s.close();
					} catch (IOException e1) {
						/* we don't care for this one, we just want to
						 *  get rid of the socket and start over. */
					}
				}
			}

			logger.warn("terminating " + (shutdownRequested() ? "as requested" : "abnormally"));
		}

		private boolean shutdownRequested() {
			return false;//TODO
		}

		private Socket connectSocket() throws InterruptedException {
			Socket s = null;
			do {
				try {
					s = new Socket(rcvHost, rcvPort);
				} catch (IOException e) {
					logger.warn("could not connect to receiver at " + rcvHost + ":" + rcvPort + " - retrying");
					Thread.sleep(1000);
				}
			} while (s == null);

			return s;
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
		private final Logger logger = LoggerFactory.getLogger(ArasLiveReplicator.Receiver.class);

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
				logger.warn("Input didn't match: '" + line + "'");
				return;
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
				onEndOfTx(txSeq);
				return;
			}

			boolean added = m.group(3).equals("+");

			if (m.group(4).charAt(0) == 'N') {
				onNodeOp(txSeq, added, new SimpleResourceID(m.group(5)));
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

				onRelOp(txSeq, added, new DetachedStatement(sub, pred, obj)); //what about contexts? XXX
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
						logger.warn("Receiver read EOF");
						r.close();
						return; // for now. TODO: make it relisten
					}
					process(line);
				}
			} catch (IOException e) {
				logger.warn("Receiver caught IOException");
				e.printStackTrace();
			}
			logger.warn("Receiver thread terminating");
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
