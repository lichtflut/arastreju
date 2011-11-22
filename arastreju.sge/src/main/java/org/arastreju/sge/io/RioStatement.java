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

import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
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
public class RioStatement implements Statement {

	private final Association assoc;

	// -----------------------------------------------------
	
	/**
	 * @param assoc
	 */
	public RioStatement(final Association assoc) {
		this.assoc = assoc;
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.openrdf.model.Statement#getContext()
	 */
	public Resource getContext() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openrdf.model.Statement#getObject()
	 */
	public Value getObject() {
		final SemanticNode object = assoc.getObject();
		if (object.isResourceNode()){
			return new URIImpl(object.asResource().getQualifiedName().toURI());
		} else {
			return new LiteralImpl(object.asValue().getStringValue());	
		}
	}

	/* (non-Javadoc)
	 * @see org.openrdf.model.Statement#getPredicate()
	 */
	public URI getPredicate() {
		return new URIImpl(assoc.getPredicate().getQualifiedName().toURI());
	}

	/* (non-Javadoc)
	 * @see org.openrdf.model.Statement#getSubject()
	 */
	public Resource getSubject() {
		final ResourceNode subject = assoc.getSubject();
		if (subject.isBlankNode()){
			return new BNodeImpl(subject.getQualifiedName().getSimpleName());
		}
		return new URIImpl(subject.getQualifiedName().toURI());
	}

}
