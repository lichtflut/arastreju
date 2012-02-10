/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.traverse;

import java.io.Serializable;

import org.arastreju.sge.model.Statement;

/**
 * <p>
 *  Filter for statements.
 * </p>
 *
 * <p>
 * 	Created Dec 22, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface TraversalFilter extends Serializable {
	
	TraverseCommand accept(Statement stmt);

}
