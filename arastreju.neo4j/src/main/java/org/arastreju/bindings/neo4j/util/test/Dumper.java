/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
