/*
 *  Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.repl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.*;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;

/**
 * <p>
 *  Crude Socket-based Transaction replicator, sender part.
 *  Holds/operates a Dispatcher for actual data delivery
 * </p>
 *
 * Created: 16.11.2012
 *
 * @author Timo Buhrmester
 */
public class ArasLiveReplicator {
	private List<GraphOp> replLog;
	private Dispatcher dispatcher;
	private int txSeq = 0;

	// -- CONSTRUCTOR -------------------------------------

	public ArasLiveReplicator() {
		replLog = new LinkedList<GraphOp>();
		new Thread(dispatcher = new Dispatcher()).start();
	}

	// ----------------------------------------------------

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

	
	// -- INNER CLASSES -----------------------------------

	/**
	 * <p>
	 *  Maintains a socket connection to the receiving part,
	 *  single task is to transmit whatever appears on our sendQ
	 * </p>
	 *
	 * Created: 16.11.2012
	 *
	 * @author Timo Buhrmester
	 */
	private class Dispatcher implements Runnable {
		private final List<String> sendQ = new LinkedList<String>();

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
			boolean dead = false;
			try {
				Socket s = new Socket("localhost", 12345); // XXX
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
					w.write(item);
					w.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	
	/**
	 * <p>
	 *  Represents a (positive or negative) graph operation
	 * </p>
	 *
	 * Created: 16.11.2012
	 *
	 * @author Timo Buhrmester
	 */
	private abstract class GraphOp {
		private final boolean add;
		
		// ------------------------------------------------
		
		GraphOp(boolean add) {
			this.add = add;
		}

		// ------------------------------------------------
		
		public String toProtocolString() {
			return (add ? "+" : "-") + getProtocolRep();
		}

		boolean isAdded() {
			return add;
		}

		public abstract String getProtocolRep();
	}
	

	/**
	 * <p>
	 *  Represents adding/removal of a node
	 * </p>
	 *
	 * Created: 16.11.2012
	 *
	 * @author Timo Buhrmester
	 */
	private class NodeOp extends GraphOp {
		private final ResourceID node;

		// ------------------------------------------------
		
		NodeOp(boolean add, ResourceID node) {
			super(add);
			this.node = node;
		}

		// ------------------------------------------------
		
		public String getProtocolRep() {
			return "N " + node.toURI(); // N for Node, as opposed to Statement (below)
		}
	}

	/**
	 * <p>
	 *  Represents adding/removal of a statement/relation
	 * </p>
	 *
	 * Created: 16.11.2012
	 *
	 * @author Timo Buhrmester
	 */
	private class RelOp extends GraphOp {
		private final Statement stmt;

		// ------------------------------------------------
		
		RelOp(boolean add, Statement stmt) {
			super(add);
			this.stmt = stmt;
		}

		// ------------------------------------------------
		
		public String getProtocolRep() {
			String rep = "S " // this is a statement, as opposed to a node
					+ stmt.getSubject().toURI() + " " + stmt.getPredicate().toURI() + " ";

			if (stmt.getObject().isResourceNode()) {
				rep += "R " + stmt.getObject().asResource().toURI(); // R for Resource
			} else {
				rep += "V " + stmt.getObject().asValue().getDataType().name() // V for value
						+ " " + stmt.getObject().asValue().getValue().toString(); // XXX
			}

			return rep;
		}
	}
}
