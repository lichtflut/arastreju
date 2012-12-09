/*
 *  Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.repl;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.model.*;
import org.arastreju.sge.model.nodes.*;
import org.arastreju.sge.spi.ProfileCloseListener;
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
 * </p>
 *
 * Created: 16.11.2012
 *
 * @author Timo Buhrmester
 */
public abstract class ArasLiveReplicator implements ProfileCloseListener {
	private final Logger logger = LoggerFactory.getLogger(ArasLiveReplicator.class);

	private List<GraphOp> replLog;
	private Dispatcher dispatcher;
	private Receiver receiver;
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
		(receiver = new Receiver(lstAddr, lstPort, "RCV-" + lstPort)).start();
		(dispatcher = new Dispatcher(rcvHost, rcvPort, "DSP-" + rcvPort)).start();
		logger.info("replication threads started. "
		        + "receiver listens on " + lstAddr + ":" + lstPort + ", "
		        + "dispatcher connects to " + rcvHost + ":" + rcvPort);
	}

	public void shutdown() {
		dispatcher.requestShutdown();
		receiver.requestShutdown();
	}

	/* if you override this again, be sure to call super.onClosed()! */
	@Override
	public void onClosed(final ArastrejuProfile profile) {
		shutdown();
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

	/*
	 * NOTE THAT the following three functions, once implemented in
	 *   whatever subclasses this, will be called asynchronously by
	 *   a separate, rogue thread.
	 * Know thy synchronization is this is of concern!
	 */

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
	protected abstract void onEndOfTx(int txSeq, boolean success);

	// -- INNER CLASSES -----------------------------------

	/**
	 * <p>
	 *  Superclass of both Receiver and Dispatcher.
	 *  Commonly shared features are socket operations and the
	 *    ability to request/check for shutdown
	 * </p>
	 *
	 * Created: 16.11.2012
	 *
	 * @author Timo Buhrmester
	 */
	private abstract class ReplicatorThread extends Thread {

		private boolean shutdown = false;

		public ReplicatorThread(String threadName) {
			super(threadName);
		}

		public void requestShutdown() {
			synchronized (this) {
				shutdown = true;
			}
			logger().warn("shutdown requested, setting flag and interrupting thread");
			interrupt();
		}

		protected abstract Logger logger(); //for meaningful debug messages

		protected synchronized boolean shutdownRequested() {
			return shutdown;
		}

		/* try hard (i.e. retry on failure) to connect a socket. */
		protected Socket connectSocket(String host, int port) throws InterruptedException {
			Socket s = null;
			do {
				try {
					s = new Socket(host, port);
				} catch (IOException e) {
					logger.warn("could not connect to " + host + ":" + port + " - retrying");
					Thread.sleep(1000);
				}
			} while (s == null);

			return s;
		}

		/* try hard (i.e. retry on failure) to spawn a ServerSocket on ifaddr:port. */
		protected ServerSocket makeListeningSocket(String ifaddr, int port) throws InterruptedException {
			ServerSocket srv = null;

			do {
				try {
					srv = new ServerSocket(port, 0, InetAddress.getByName(ifaddr));
					/* we need a separate try/catch block to distinguish
					 * the exception from those potentially thrown by
					 * the ServerSocket() ctor. */
					try {
						srv.setSoTimeout(1000); //accept() timeout
						srv.setReuseAddress(true);
					} catch (SocketException e) {
						logger.warn("failed to enable SO_TIMEOUT or SO_REUSEADDR on ServerSocket."
						        + "We might get stuck on shutdown.");
					}

					logger.debug("bound to, and now listening on: " + ifaddr + ":" + port);

				} catch (IOException e) {
					logger.warn("could not bind()/listen() to/on "
					        + ifaddr + ":" + port + " - retrying");
					Thread.sleep(1000);
				}
			} while (srv == null);

			return srv;
		}

		/* try hard (i.e. retry on failure) to accept a connection
		 * may throw ListenerException if the given ServerSocket breaks. */
		protected Socket acceptSocket(ServerSocket srv) throws InterruptedException, ListenerException {
			Socket s = null;
			do {
				if (Thread.interrupted()) {
					throw new InterruptedException("interrupted accept() loop");
				}

				if (srv.isClosed() || !srv.isBound()) {
					throw new ListenerException();
				}

				try {
					s = srv.accept();
					try {
						s.setSoTimeout(1000);
					} catch (SocketException e) {
						logger.warn("failed to enable SO_TIMEOUT on accept()ed Socket."
						        + "We might get stuck on shutdown.");
					}
				} catch (SocketTimeoutException e) {
					// ignore, just re-eval interrupt condition
				} catch (IOException e) {
					logger.warn("failed to accept a connection (on "
					        + srv.getLocalSocketAddress() + ":"
					        + srv.getLocalPort() + " - retrying");
				}
			} while (s == null);

			return s;
		}

		/* wrapper for close(), to get rid of potential IOExceptions which may
		 *  be thrown in close()'s implicit call to flush().
		 * There are cases where it's important to catch and handle it,
		 * but the replicator isn't one (as we have our own startover mechanism). */
		protected void closeSocket(Socket s) {
			try { if (s != null) s.close(); } catch (IOException e) {}
		}

		protected void closeSocket(ServerSocket s) {
			try { if (s != null) s.close(); } catch (IOException e) {}
		}

		protected class ListenerException extends Exception {}
	}

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
	private class Dispatcher extends ReplicatorThread {
		private final Logger logger = LoggerFactory.getLogger(ArasLiveReplicator.Dispatcher.class);

		private final String rcvHost;
		private final int rcvPort;

		/* the following four members are fiddled with by multiple threads.
		 * we synchronize on 'this' when accessing any of them. */
		private final Map<Integer, List<String>> txMap = new HashMap<Integer, List<String>>();
		private int minTxSeq = 0; //stores the lowest (a.k.a. earliest) tx we remember
		private int curTxSeq = -1; //stores the highest tx we saw, finished or not
		private boolean curTxDone = false; //stores whether tx# curTxSeq has seen its "EOT" already

		// ------------------------------------------------

		Dispatcher(String receiverHost, int receiverPort, String threadName) {
			super(threadName);
			rcvHost = receiverHost;
			rcvPort = receiverPort;
		}

		// ------------------------------------------------

		@Override
		public void run() {
			BufferedWriter w;
			BufferedReader r;
			Socket s = null;

			int nextTxSeq; //stores the next tx we're going to send

			while (!shutdownRequested()) {
				try {
					s = connectSocket(rcvHost, rcvPort); //retries on failure, except when interrupted
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

							synchronized (this) {
								/* minTxSeq does have a value >= 0 by now, since
								 * /something/ must have been queue()d in the past
								 * for the receiver to tell us about that he got it */
								for (int i = minTxSeq; i <= good; i++) {
									txMap.remove(i);
								}
								minTxSeq = good + 1;
							}

						}

						synchronized (this) {
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
									wait(1000); //arbitrary timeout, 1s
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
							logger.debug("writing '" + line + "'");
							w.write(nextTxSeq + " " + line + "\n");
						}
						w.flush();
						logger.debug("dispatched tx#" + nextTxSeq);
						nextTxSeq++;
					}
				} catch (IOException e) { //thrown by get{In,Out}putStream(), readLine(), write(), flush()
					e.printStackTrace();
				} finally {
					closeSocket(s);
				}
				logger.info("at end of life loop - starting over");
			}

			logger.warn("terminating " + (shutdownRequested() ? "as requested" : "abnormally"));
		}

		// ------------------------------------------------

		synchronized void queue(int txSeq, String s) {
			List<String> l = txMap.get(txSeq);

			if (l == null)
				txMap.put(txSeq, (l = new LinkedList<String>()));

			l.add(s);

			curTxSeq = txSeq;
			curTxDone = s.equals("EOT");

			notifyAll();
		}

		// ------------------------------------------------

		@Override
		protected Logger logger() {
			return logger;
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
	private class Receiver extends ReplicatorThread {
		private final Logger logger = LoggerFactory.getLogger(ArasLiveReplicator.Receiver.class);

		private final String lstAddr;
		private final int lstPort;

		private int lastComplTx = -1;

		// ------------------------------------------------

		/**
		 *
		 * @param listenAddr the interface to listen() on, may be null to
		 *   listen on all available interfaces (a.k.a. INADDR_ANY (most
		 *   usually equivalent to "0.0.0.0").
		 * @param string
		 * @param receiverPort the port to listen() on, >= 1
		 */
		Receiver(String listenAddr, int listenPort, String threadName) {
			super(threadName);
			lstAddr = listenAddr;
			lstPort = listenPort;
		}

		// ------------------------------------------------

		@Override
		public void run() {

			BufferedReader r;
			BufferedWriter w;
			Socket s = null;
			ServerSocket srv = null;

			int inTx = -1;

			/* first we try to get a server going */
			while (srv == null && !shutdownRequested()) {
				try {
					srv = makeListeningSocket(lstAddr, lstPort);
				} catch (InterruptedException e) {
					//re-eval shutdown condition
				}
			}

			while (!shutdownRequested()) {
				try {
					s = acceptSocket(srv); //retries on failure, except when interrupted

					logger.debug("accepted a socket (on " + lstAddr + ":" + lstPort
					        + ", peer: " + s.getRemoteSocketAddress() + ":" + s.getPort() + ")");
				} catch (InterruptedException e) {
					logger.warn("interrupted while listening/accepting (on "
					        + lstAddr + ":" + lstPort + ")");
					continue; //re-evaluate shutdown condition
				} catch (ListenerException e) {
					logger.warn("something's wrong with the ServerSocket. "
					        + "bound: " + srv.isBound() + ", closed: " + srv.isClosed());

					/* try to reinit the listener */
					closeSocket(srv);
					srv = null;

					while (srv == null && !shutdownRequested()) {
						try {
							srv = makeListeningSocket(lstAddr, lstPort);
						} catch (InterruptedException e2) {
							//re-eval shutdown condition
						}
					}

					continue;
				}

				if (inTx != -1) { //we were in the middle of a transaction, ditch it
					onEndOfTx(inTx, false);
				}

				try {
					w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					r = new BufferedReader(new InputStreamReader(s.getInputStream()));

					/* tell other end where to start */
					w.write(lastComplTx + "\n");
					w.flush();

					while (!shutdownRequested()) {
						String line;
						try {
							line = r.readLine();
						} catch (SocketTimeoutException e) {
							continue; //re-eval shutdown condition
						}

						if (line == null) { /* at EOF */
							logger.warn("Receiver read EOF");
							/* EOF is actually not supposed to show up, except
							 * perhaps in case of peer's shutdown, anyway we're
							 * nice and cleanly dispose of the connection too,
							 * then start over like we would if the connection
							 * broke the hard way */
							r.close();
							break;
						}

						if ((line = line.trim()).length() == 0)
							continue;

						logger.debug("read: '" + line + "'");

						int curTx = process(line);

						boolean isEOT = line.endsWith("EOT");
						inTx = isEOT ? -1 : curTx;

						if (isEOT) {
							lastComplTx = curTx;
							w.write(lastComplTx + "\n"); //tell dispatcher about it
							w.flush();
						}
					}
				} catch (IOException e) { //thrown by get{In,Out}putStream(), write(), flush(), readLine(), close()
					e.printStackTrace();
				} finally {
					closeSocket(s);
				}

				logger.info("at end of life loop - starting over");
			}

			if (inTx != -1) { //we were in the middle of a transaction
				onEndOfTx(inTx, false);
			}

			logger.warn("terminating " + (shutdownRequested() ? "as requested" : "abnormally"));
			closeSocket(srv);
		}

		// ------------------------------------------------

		@Override
		protected Logger logger() {
			return logger;
		}

		// ------------------------------------------------

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
		 * rel operation with resource object: e.g. "1337 -S http://so.me/subj http://the.pred/icate R http://so.me/resource"
		 * 	group 1: sequence number (1337)
		 *  group 3: [+-] (-)
		 * 	group 6: subject (http://so.me/subj)
		 *  group 7: predicate (http://the.pred/icate)
		 *  group 9: value (http://so.me/resource)
		 */
		private int process(String line) {
			Matcher m = MatcherProvider.matcher(line);

			if (!m.matches()) {
				logger.warn("Input didn't match: '" + line + "'");
				throw new IllegalArgumentException("bogus input: '" + line + "')");
			}

			int txSeq = Integer.parseInt(m.group(1));
			if (m.group(2).equals("EOT")) {
				onEndOfTx(txSeq, true);
				return txSeq;
			}

			boolean added = m.group(3).equals("+");

			if (m.group(4).charAt(0) == 'N') {
				onNodeOp(txSeq, added, new SimpleResourceID(m.group(5)));
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

			return txSeq;
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
