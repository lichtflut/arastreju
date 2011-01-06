/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;



/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Nov 5, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface TxAction {

	/**
	 * Execute.
	 */
	public void execute(NeoDataStore store);

}
