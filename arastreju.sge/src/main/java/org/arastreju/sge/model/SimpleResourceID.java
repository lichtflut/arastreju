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
package org.arastreju.sge.model;

import java.io.Serializable;
import java.util.UUID;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.naming.VoidNamespace;

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
	
	private final QualifiedName qualifiedName;
	
	// -----------------------------------------------------

	/**
	 * Creates a new unique ResourceID with random URI in {@link VoidNamespace}.
	 */
	public SimpleResourceID() {
		this(VoidNamespace.getURI(), UUID.randomUUID().toString());
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
		this(new QualifiedName(uri));
	}
	
	/**
	 * Constructor.
	 * @param nsUri The namespace URI.
	 * @param name The simple name.
	 */
	public SimpleResourceID(final String nsUri, final String name) {
		this(new QualifiedName(nsUri, name));
	}
	
	// ----------------------------------------------------
	
	public QualifiedName getQualifiedName() {
		return qualifiedName;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public String toURI() {
		return qualifiedName.toURI();
	}
	
	// ----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isResourceNode() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValueNode() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAttached() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public ResourceNode asResource() {
		return new SNResource(qualifiedName);
	}

	/**
	 * {@inheritDoc}
	 */
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
