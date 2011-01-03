/*
 * Copyright (C) 2009 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.util.test;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Dec 16, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class Dumper {
	
	public static String dump(final Node neoNode){
		final StringBuilder sb = new StringBuilder("node[" + neoNode.getId() + "|" + System.identityHashCode(neoNode) + "]");
		return sb.toString();
	}
	
	public static String dumpDeep(final Node neoNode){
		final StringBuilder sb = new StringBuilder("node[" + neoNode.getId() + "|" + System.identityHashCode(neoNode) + "]");
		for (Relationship rel : neoNode.getRelationships()) {
			sb.append("\n * " + dump(rel));
		}
		return sb.toString();
	}
	
	public static String dump(final Relationship rel){
		final StringBuilder sb = new StringBuilder("rel[" + rel.getId() + "|" + System.identityHashCode(rel) + "] ");
		sb.append(dump(rel.getStartNode()) + " --> " + dump(rel.getEndNode()));
		return sb.toString();
	}

}
