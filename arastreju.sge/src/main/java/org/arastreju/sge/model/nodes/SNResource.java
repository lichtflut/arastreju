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

import java.util.HashSet;
import java.util.Set;

import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.associations.AssociationKeeper;
import org.arastreju.sge.model.associations.DetachedAssociationKeeper;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNProperty;
import org.arastreju.sge.model.nodes.views.SNPropertyDeclaration;
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
public class SNResource implements ResourceNode {
	
	private String name;
	
	private Namespace namespace = VoidNamespace.getInstance();
	
	private AssociationKeeper associationKeeper;
	
	// ------------------------------------------------------
	
	/**
	 * Default constructor for new unattached resource.
	 */
	public SNResource() {
		this.associationKeeper = new DetachedAssociationKeeper();
	}
	
	/**
	 * Default constructor for new unattached resource.
	 */
	public SNResource(final Namespace namespace, final String name) {
		this(namespace, name, new DetachedAssociationKeeper());
	}
	
	
	/**
	 * Default constructor for new unattached resource.
	 */
	public SNResource(final QualifiedName qn) {
		this(qn.getNamespace(), qn.getSimpleName(), new DetachedAssociationKeeper());
	}
	
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

	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ResourceNode#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ResourceNode#getNamespace()
	 */
	public Namespace getNamespace() {
		return namespace;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ResourceNode#getQualifiedName()
	 */
	public QualifiedName getQualifiedName() {
		return new QualifiedName(namespace, name);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ResourceNode#setName(java.lang.String)
	 */
	public void setName(final String name) {
		// TODO: make protected: Only changeable in SPI
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ResourceNode#setNamespace(org.arastreju.sge.naming.Namespace)
	 */
	public void setNamespace(final Namespace namespace) {
		// TODO: make protected: Only changeable in SPI
		this.namespace = namespace;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ResourceNode#isBlankNode()
	 */
	public boolean isBlankNode() {
		return VoidNamespace.isInVoidNamespace(this);
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#getUri()
	 */
	public String getUri() {
		return getQualifiedName().toURI();
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#references(org.arastreju.api.ontology.model.ResourceID)
	 */
	public boolean references(final ResourceID ref) {
		return getQualifiedName().equals(ref.getQualifiedName());
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#isAttached()
	 */
	public boolean isAttached() {
		return associationKeeper.isAttached();
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#isResourceNode()
	 */
	public boolean isResourceNode() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#isValueNode()
	 */
	public boolean isValueNode() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.SemanticNode#asResource()
	 */
	public ResourceNode asResource() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.SemanticNode#asValue()
	 */
	public ValueNode asValue() {
		throw new IllegalStateException("Not a value: " + this);
	}
	
	// -- ASSOCIATIONS ------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#getAssociations()
	 */
	public synchronized Set<Association> getAssociations() {
		return associationKeeper.getAssociations();
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#getSingleAssociation(org.arastreju.api.ontology.model.ResourceID)
	 */
	public Association getSingleAssociation(final ResourceID predicate) {
		for (Association assoc : getAssociations()) {
			if (predicate.references(assoc.getPredicate())) {
				return assoc;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#getSingleAssociationClient(org.arastreju.api.ontology.model.ResourceID)
	 */
	public SemanticNode getSingleAssociationClient(final ResourceID predicate) {
		final Association assoc = getSingleAssociation(predicate);
		if (assoc != null){
			return assoc.getClient();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#getAssociations(org.arastreju.api.ontology.model.ResourceID)
	 */
	public Set<Association> getAssociations(final ResourceID predicate) {
		Set<Association> result = new HashSet<Association>();
		for (Association assoc : getAssociations()) {
			final ResourceID assocPred = assoc.getPredicate();
			if (!RDFS.SUB_PROPERTY_OF.equals(predicate) && assocPred.isAttached()){
				final SNProperty property = assocPred.asResource().asProperty();
				if (property.isSubPropertyOf(predicate)){
					result.add(assoc);
				}
			} else if (predicate.references(assoc.getPredicate())) {
				result.add(assoc);
			}
		}
		return result;
	}
	
	public Set<SemanticNode> getAssociationClients(final ResourceID predicate) {
		Set<SemanticNode> result = new HashSet<SemanticNode>();
		for (Association assoc : getAssociations()) {
			final ResourceID assocPred = assoc.getPredicate();
			if (assocPred instanceof SNProperty){
				final SNProperty property = (SNProperty) assocPred;
				if (property.isSubPropertyOf(predicate)){
					result.add(assoc.getClient());
				}
			} else if (predicate.references(assoc.getPredicate())) {
				result.add(assoc.getClient());
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#hasAssociation(org.arastreju.api.ontology.model.Association)
	 */
	public boolean hasAssociation(final Association assoc) {
		return getAssociations().contains(assoc);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#addToAssociations(org.arastreju.api.ontology.model.Association)
	 */
	public void addToAssociations(final Association assoc) {
		if (!assoc.getSupplier().equals(this)){
			throw new IllegalArgumentException();
		}
		associationKeeper.add(assoc);
	}
	
	// -- DENY ASSOC --------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#revokeAssociation(org.arastreju.api.ontology.model.Association)
	 */
	public boolean revoke(Association assoc) {
		return associationKeeper.revoke(assoc);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#remove(org.arastreju.api.ontology.model.Association)
	 */
	public void remove(final Association assoc){
		associationKeeper.remove(assoc);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#reset()
	 */
	public void reset() {
		associationKeeper.reset();
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#asEntity()
	 */
	public SNEntity asEntity() {
		return new SNEntity(this);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#asClass()
	 */
	public SNClass asClass() {
		return new SNClass(this);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#asProperty()
	 */
	public SNProperty asProperty(){
		return new SNProperty(this);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#asPropertyDeclaration()
	 */
	public SNPropertyDeclaration asPropertyDeclaration(){
		return new SNPropertyDeclaration(this);
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
			return references((ResourceID) obj);
		}
		return false;
	}

}
