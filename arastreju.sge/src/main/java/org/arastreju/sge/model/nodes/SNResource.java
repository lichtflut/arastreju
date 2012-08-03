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
package org.arastreju.sge.model.nodes;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNProperty;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * <p>
 * 	Base for all semantic nodes representing a resource (instead of a value).
 * 	A resource may have incoming and outgoing associations to other resources.
 * 	There can be defined resource descriptions (which are also expressed by associations).
 * </p>
 * 
 * <p>
 * 	Created: 14.08.2009
 * </p>
 *
 * @author Oliver Tigges
 */
public class SNResource implements ResourceNode, Serializable {
	
	private final QualifiedName qn;
	
	private final int hash; 
	
	private AssociationKeeper associationKeeper;
	
	// -- PUBLIC CONSTRUCTORS -----------------------------
	
	/**
	 * Default constructor for new unattached resource.
	 */
	public SNResource() {
		this(QualifiedName.create(Namespace.UUID, UUID.randomUUID().toString()));
	}
	
	/**
	 * Default constructor for new unattached resource.
	 */
	public SNResource(final QualifiedName qn) {
		this(qn, new DetachedAssociationKeeper());
	}
	
	// -- PROTECTED CONSTRUCTORS --------------------------
	
	/**
	 * Constructor for SPI subclasses.
	 * @param qn The qualified name.
	 * @param associationKeeper
	 */
	protected SNResource(final QualifiedName qn, final AssociationKeeper associationKeeper) {
		this.qn = qn;
		this.associationKeeper = associationKeeper;
		this.hash = qn.hashCode();
	}
	
	// ------------------------------------------------------

    @Override
	public QualifiedName getQualifiedName() {
		return qn;
	}

    @Override
	public String toURI() {
		return qn.toURI();
	}

    @Override
	public boolean isBlankNode() {
		return false;
	}
	
	// -----------------------------------------------------

    @Override
	public boolean isAttached() {
		return associationKeeper.isAttached();
	}

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
		return this;
	}

    @Override
	public ValueNode asValue() {
		throw new IllegalStateException("Not a value: " + this);
	}
	
	// -- ASSOCIATIONS ------------------------------------
	
	@Override
	public Set<Statement> getAssociations() {
		return Collections.unmodifiableSet(associationKeeper.getAssociations());
	}

    @Override
	public Set<Statement> getAssociations(final ResourceID predicate) {
		final Set<Statement> result = new HashSet<Statement>();
		for (Statement assoc : getAssociations()) {
			if (predicate.equals(assoc.getPredicate())) {
				result.add(assoc);
			}
		}
		return result;
	}

    @Override
	public Statement addAssociation(ResourceID predicate, SemanticNode object, Context... ctx) {
		final Statement statement = new DetachedStatement(this, predicate, object, ctx);
		associationKeeper.addAssociation(statement);
		return statement;
	}
	
	// -- DENY ASSOC --------------------------------------

    @Override
	public boolean removeAssociation(final Statement stmt){
		return associationKeeper.removeAssociation(stmt);
	}
	
	// -----------------------------------------------------

    @Override
	public SNEntity asEntity() {
		return new SNEntity(this);
	}

    @Override
	public SNClass asClass() {
		return new SNClass(this);
	}

    @Override
	public SNProperty asProperty(){
		return new SNProperty(this);
	}
	
	// ------------------------------------------------------
	
	@Override
	public String toString() {
		return toURI();
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ResourceID) {
			final ResourceID other = (ResourceID) obj;
			return qn.equals(other.getQualifiedName());
		}
		return false;
	}

}
