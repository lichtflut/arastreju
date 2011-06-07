/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.tx;

/**
 * <p>
 *  Interface for wrappers of a Neo4j transaction.
 * </p>
 *
 * <p>
 * 	Created Jun 7, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface TxWrapper {
	
	void markSuccessful();
	
	void markFailure();
	
	void finish();
	
	boolean isFailure();
	
}
