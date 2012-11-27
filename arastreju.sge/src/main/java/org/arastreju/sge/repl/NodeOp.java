package org.arastreju.sge.repl;

import org.arastreju.sge.model.ResourceID;

/**
 * <p>
 *  Represents adding/removal of a node
 * </p>
 *
 * Created: 16.11.2012
 *
 * @author Timo Buhrmester
 */
public class NodeOp extends GraphOp {
	private final ResourceID node;

	// ------------------------------------------------

	public NodeOp(boolean add, ResourceID node) {
		super(add);
		this.node = node;
	}

	// ------------------------------------------------

	public ResourceID getNode() {
		return node;
	}
	
	public String getProtocolRep() {
		return "N " + node.toURI(); // N for Node, as opposed to Statement (below)
	}
	
	public boolean equals(Object o) {
		if (o instanceof NodeOp) {
			return super.equals(o) && node.equals(((NodeOp)o).node);
		}
		return false;
	}
}
