/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.bindings.neo4j.util.test;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * <p>
 *  Dumper for Nodes and Relationships.
 * </p>
 *
 * <p>
 * 	Created Dec 16, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoGraphDumper implements NeoConstants {
	
	/**
	 * Dumps just the given Neo node.
	 * @param neoNode The node to be dumped. 
	 * @return The corresponding string.
	 */
	public static String dump(final Node neoNode){
		final StringBuilder sb = new StringBuilder("node[");
		if (neoNode.hasProperty(PROPERTY_URI)){
			sb.append(neoNode.getProperty(PROPERTY_URI));
		} else if (neoNode.hasProperty(PROPERTY_VALUE)) {
			sb.append(neoNode.getProperty(PROPERTY_VALUE));
		} else {
			sb.append(neoNode.getId() + "|" + System.identityHashCode(neoNode));
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Dumps the given Neo node with all relations.
	 * @param neoNode The node to be dumped. 
	 * @return The corresponding string.
	 */
	public static String dumpDeep(final Node neoNode){
		final StringBuilder sb = new StringBuilder("node[" + neoNode.getId() + "|" + 
				System.identityHashCode(neoNode) + "]");
		for (Relationship rel : neoNode.getRelationships()) {
			sb.append("\n * " + dump(rel));
		}
		return sb.toString();
	}
	
	/**
	 * Dumps the relation.
	 * @param rel The relation.
	 * @return The corresponding string.
	 */
	public static String dump(final Relationship rel){
		final StringBuilder sb = new StringBuilder("rel[");
		sb.append(dump(rel.getStartNode()));
		if (rel.hasProperty(PREDICATE_URI)){
			sb.append(rel.getProperty(PREDICATE_URI));
		} else {
			sb.append(rel.getId() + "|" + System.identityHashCode(rel));
		}
		sb.append(dump(rel.getEndNode()));
		return sb.toString();
	}

}
