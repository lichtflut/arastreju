/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.traverse;

import org.arastreju.sge.model.Statement;

/**
 * <p>
 *  Common interface for visited statments.
 * </p>
 *
 * <p>
 * 	Created Feb 28, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public interface StatementVisitor {

	void visit(Statement stmt);
	
}
