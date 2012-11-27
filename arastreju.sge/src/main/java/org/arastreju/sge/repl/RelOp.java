package org.arastreju.sge.repl;

import org.arastreju.sge.model.Statement;

/**
 * <p>
 *  Represents adding/removal of a statement/relation
 * </p>
 *
 * Created: 16.11.2012
 *
 * @author Timo Buhrmester
 */
public class RelOp extends GraphOp {
	private final Statement stmt;

	// ------------------------------------------------

	public RelOp(boolean add, Statement stmt) {
		super(add);
		this.stmt = stmt;
	}

	// ------------------------------------------------

	public Statement getStatement() {
		return stmt;
	}
	
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

	public boolean equals(Object o) {
		if (o instanceof RelOp) {
			return super.equals(o) && stmt.equals(((RelOp)o).stmt);
		}
		return false;
	}
	
}
