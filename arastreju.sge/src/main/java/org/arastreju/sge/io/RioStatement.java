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
package org.arastreju.sge.io;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.URIImpl;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class RioStatement implements org.openrdf.model.Statement {

	private final org.arastreju.sge.model.Statement arasStmt;

	// -----------------------------------------------------
	
	/**
	 * @param stmt
	 */
	public RioStatement(final org.arastreju.sge.model.Statement stmt) {
		this.arasStmt = stmt;
	}
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public Resource getContext() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Value getObject() {
		final SemanticNode object = arasStmt.getObject();
		if (object.isResourceNode()){
			return new URIImpl(object.asResource().getQualifiedName().toURI());
		} else {
			return new LiteralImpl(object.asValue().getStringValue());	
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public URI getPredicate() {
		return new URIImpl(arasStmt.getPredicate().getQualifiedName().toURI());
	}

	/**
	 * {@inheritDoc}
	 */
	public Resource getSubject() {
		final ResourceID subject = arasStmt.getSubject();
		if (subject.asResource().isBlankNode()){
			return new BNodeImpl(subject.getQualifiedName().getSimpleName());
		}
		return new URIImpl(subject.getQualifiedName().toURI());
	}

}
