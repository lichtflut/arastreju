/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.model;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;

import java.io.Serializable;

/**
 * <p>
 * 	Simple implementation of transient {@link ResourceID}.
 * </p>
 * 
 * <p>
 * 	Created: 02.02.2009
 * </p>
 *
 * @author Oliver Tigges
 */
public class SimpleResourceID implements ResourceID, Serializable {

    public static SimpleResourceID from(QualifiedName qn) {
        return new SimpleResourceID(qn);
    }

    // ----------------------------------------------------
	
	private final QualifiedName qualifiedName;
	
	// -----------------------------------------------------

	/**
	 * Creates a new unique ResourceID with random URI in Namespace.UUID namespace.
	 */
	public SimpleResourceID() {
		this(QualifiedName.generate());
	}
	
	/**
	 * Create a ResourceID based on {@link QualifiedName}.
	 * @param qn The qualified name.
	 */
	public SimpleResourceID(final QualifiedName qn) {
		this.qualifiedName = qn;
	}
	
	/**
	 * Constructor.
	 * @param uri The URI.
	 */
	public SimpleResourceID(final String uri) {
		this(QualifiedName.from(uri));
	}
	
	/**
	 * Constructor.
	 * @param nsUri The namespace URI.
	 * @param name The simple name.
	 */
	public SimpleResourceID(final String nsUri, final String name) {
		this(nsUri + name);
	}
	
	// ----------------------------------------------------

    @Override
	public QualifiedName getQualifiedName() {
		return qualifiedName;
	}

    @Override
	public String toURI() {
		return qualifiedName.toURI();
	}
	
	// ----------------------------------------------------

    @Override
	public boolean isResourceNode() {
		return true;
	}

    @Override
	public boolean isValueNode() {
		return false;
	}

    @Override
	public ResourceNode asResource() {
		return new SNResource(qualifiedName);
	}

    @Override
	public ValueNode asValue() {
		throw new UnsupportedOperationException();
	}

	// -----------------------------------------------------
	
	@Override
	public int hashCode() {
		return qualifiedName.toURI().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ResourceID){
			final ResourceID other = (ResourceID) obj;
			return qualifiedName.equals(other.getQualifiedName());
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return qualifiedName.toString();
	}
	
}
