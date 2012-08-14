/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb;

import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created 14.08.2012
 * </p>
 *
 * @author Raphael Esterle

 */
public class SNResourceRdb extends SNResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6763966628967338553L;
	
	public SNResourceRdb(final QualifiedName qn, final AssociationKeeper keeper){
		super(qn, keeper);
	}

}
