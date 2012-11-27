package org.arastreju.sge.repl;

/**
 * <p>
 *  Represents a (positive or negative) graph operation
 *  Externalized for unit testing
 * </p>
 *
 * Created: 16.11.2012
 *
 * @author Timo Buhrmester
 */
public abstract class GraphOp {
	private final boolean add;

	// ------------------------------------------------

	public GraphOp(boolean add) {
		this.add = add;
	}

	// ------------------------------------------------

	public String toProtocolString() {
		return (add ? "+" : "-") + getProtocolRep();
	}

	public boolean isAdded() {
		return add;
	}

	public abstract String getProtocolRep();
	
	public boolean equals(Object o) {
		if (o instanceof GraphOp) {
			return ((GraphOp)o).add == add;
		}
		return false;
	}
}
