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
package org.arastreju.sge.model.nodes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNProperty;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.naming.VoidNamespace;

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
	
	private String name;
	
	private Namespace namespace;
	
	private AssociationKeeper associationKeeper;
	
	// -- PUBLIC CONSTRUCTORS -----------------------------
	
	/**
	 * Default constructor for new unattached resource.
	 */
	public SNResource() {
		this(VoidNamespace.getInstance(), UUID.randomUUID().toString());
	}
	
	/**
	 * Default constructor for new unattached resource.
	 */
	public SNResource(final QualifiedName qn) {
		this(qn.getNamespace(), qn.getSimpleName(), new DetachedAssociationKeeper());
	}
	
	/**
	 * Default constructor for new unattached resource.
	 */
	public SNResource(final Namespace namespace, final String name) {
		this(namespace, name, new DetachedAssociationKeeper());
	}
	
	// -- PROTECTED CONSTRUCTORS --------------------------
	
	/**
	 * Constructor for SPI subclasses.
	 * @param name The local name.
	 * @param namespace The namespace.
	 * @param associationKeeper
	 */
	protected SNResource(final QualifiedName qn, final AssociationKeeper associationKeeper) {
		this(qn.getNamespace(), qn.getSimpleName(), associationKeeper);
	}
	
	/**
	 * Constructor for SPI subclasses.
	 * @param namespace The namespace.
	 * @param name The local name.
	 * @param associationKeeper
	 */
	protected SNResource(final Namespace namespace, final String name, final AssociationKeeper associationKeeper) {
		this.name = name;
		this.namespace = namespace;
		this.associationKeeper = associationKeeper;
	}
	
	// ------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Namespace getNamespace() {
		return namespace;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public QualifiedName getQualifiedName() {
		return new QualifiedName(namespace, name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isBlankNode() {
		return false;
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public String getUri() {
		return getQualifiedName().toURI();
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAttached() {
		return associationKeeper.isAttached();
	}
	
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
	public ResourceNode asResource() {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ValueNode asValue() {
		throw new IllegalStateException("Not a value: " + this);
	}
	
	// -- ASSOCIATIONS ------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public synchronized Set<Association> getAssociations() {
		return associationKeeper.getAssociations();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Set<Association> getAssociations(final ResourceID predicate) {
		Set<Association> result = new HashSet<Association>();
		for (Association assoc : getAssociations()) {
			final ResourceID assocPred = assoc.getPredicate();
			final SNProperty property = assocPred.asResource().asProperty();
			if (!RDFS.SUB_PROPERTY_OF.equals(predicate) && property.isAttached()){
				if (property.isSubPropertyOf(predicate)){
					result.add(assoc);
				}
			} else if (predicate.equals(assoc.getPredicate())) {
				result.add(assoc);
			}
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addToAssociations(final Association assoc) {
		if (!assoc.getSubject().equals(this)){
			throw new IllegalArgumentException();
		}
		associationKeeper.add(assoc);
	}
	
	// -- DENY ASSOC --------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public boolean remove(final Association assoc){
		return associationKeeper.remove(assoc);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		associationKeeper.clearAssociations();
	}
	
	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public SNEntity asEntity() {
		return new SNEntity(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public SNClass asClass() {
		return new SNClass(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public SNProperty asProperty(){
		return new SNProperty(this);
	}
	
	// ------------------------------------------------------
	
	@Override
	public String toString() {
		return getUri();
	}
	
	@Override
	public int hashCode() {
		return getUri().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ResourceID) {
			final ResourceID other = (ResourceID) obj;
			return getQualifiedName().equals(other.getQualifiedName());
		}
		return false;
	}

}
