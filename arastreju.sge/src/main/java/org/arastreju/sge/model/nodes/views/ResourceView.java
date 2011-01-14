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
package org.arastreju.sge.model.nodes.views;

import java.util.Set;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *	Abstract base for all classes that provide a view on a {@link SNResource}. 
 * </p>
 * 
 * <p>
 * 	Created Nov 29, 2009
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class ResourceView implements ResourceNode {

	private final ResourceNode resource;
	
	// ------------------------------------------------------
	
	/**
	 * Creates a new view to the given resource.
	 * @param resource The resource to be wrapped.
	 */
	public ResourceView(final ResourceNode resource) {
		this.resource = resource;
	}
	
	/**
	 * Creates a view to a resource to be created implicitly.
	 */
	protected ResourceView() {
		this.resource = new SNResource();
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#getName()
	 */
	public String getName() {
		return resource.getName();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#getNamespace()
	 */
	public Namespace getNamespace() {
		return resource.getNamespace();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#getQualifiedName()
	 */
	public QualifiedName getQualifiedName() {
		return resource.getQualifiedName();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#setName(java.lang.String)
	 */
	public void setName(final String name) {
		resource.setName(name);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#setNamespace(org.arastreju.api.ontology.Namespace)
	 */
	public void setNamespace(final Namespace namespace) {
		resource.setNamespace(namespace);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.ResourceID#references(org.arastreju.api.ontology.model.ResourceID)
	 */
	public boolean references(final ResourceID ref) {
		return resource.references(ref);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ResourceNode#isBlankNode()
	 */
	public boolean isBlankNode() {
		return resource.isBlankNode();
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.IResource#getAssociations()
	 */
	public Set<Association> getAssociations() {
		return resource.getAssociations();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.IResource#getAssociations(org.arastreju.api.ontology.model.ResourceID)
	 */
	public Set<Association> getAssociations(final ResourceID predicate) {
		return resource.getAssociations(predicate);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#getClients(org.arastreju.api.ontology.model.ResourceID)
	 */
	public Set<SemanticNode> getAssociationClients(final ResourceID predicate) {
		return resource.getAssociationClients(predicate);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.IResource#getSingleAssociation(org.arastreju.api.ontology.model.ResourceID)
	 */
	public Association getSingleAssociation(final ResourceID predicate) {
		return resource.getSingleAssociation(predicate);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#getSingleAssociationClient(org.arastreju.api.ontology.model.ResourceID)
	 */
	public SemanticNode getSingleAssociationClient(final ResourceID predicate) {
		return resource.getSingleAssociationClient(predicate);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.IResource#hasAssociation(org.arastreju.api.ontology.model.Association)
	 */
	public boolean hasAssociation(final Association assoc) {
		return resource.hasAssociation(assoc);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.IResource#removeAssociation(org.arastreju.api.ontology.model.Association)
	 */
	public boolean revoke(final Association assoc) {
		return resource.revoke(assoc);
	}
	
	public void removeAssociations(final ResourceID predicate){
		for (Association assoc : getAssociations(predicate)) {
			remove(assoc);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#addToAssociations(org.arastreju.api.ontology.model.Association)
	 */
	public void addToAssociations(final Association assoc) {
		resource.addToAssociations(assoc);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#removeDirectly(org.arastreju.api.ontology.model.Association)
	 */
	public void remove(final Association assoc) {
		resource.remove(assoc);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#reset()
	 */
	public void reset() {
		resource.reset();
	}
	
	// ------------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.IResource#isValueNode()
	 */
	public boolean isValueNode() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.IResource#isResourceNode()
	 */
	public boolean isResourceNode() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#isAttached()
	 */
	public boolean isAttached() {
		return resource.isAttached();
	}

	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#asClass()
	 */
	public SNClass asClass() {
		return resource.asClass();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#asEntity()
	 */
	public SNEntity asEntity() {
		return resource.asEntity();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#asProperty()
	 */
	public SNProperty asProperty() {
		return resource.asProperty();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ResourceNode#asPropertyDeclaration()
	 */
	public SNPropertyDeclaration asPropertyDeclaration() {
		return resource.asPropertyDeclaration();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#asResource()
	 */
	public ResourceNode asResource() {
		return resource;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#asValue()
	 */
	public ValueNode asValue() {
		throw new IllegalStateException("Cannot convert a resource to a value node");
	}
	
	// -----------------------------------------------------

	/**
	 * Returns the wrapped resource.
	 * @return The resource wrapped by this view.
	 */
	protected ResourceNode getResource() {
		return resource;
	}
	
	// -----------------------------------------------------
	
	@Override
	public String toString() {
		return resource.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return resource.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return resource.hashCode();
	}

}
