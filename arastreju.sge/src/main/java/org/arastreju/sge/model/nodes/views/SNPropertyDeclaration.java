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

import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;

import de.lichtflut.infra.Infra;
import de.lichtflut.infra.exceptions.NotYetImplementedException;


/**
 * Semantic node with stereotype PROPERTY_DECLARATION.
 * 
 * Represents the declaration of a property, which can be assigned
 * to a classifier.
 * 
 * Consists of a property and constraints:
 * - type
 * - cardinality (nyi)
 * - Many-In-Time-Flag (nyi)
 * 
 * Created: 20.01.2009
 *
 * @author Oliver Tigges
 */
public class SNPropertyDeclaration extends ResourceView implements Comparable<SNPropertyDeclaration>{

	/**
	 * Constructor for a new property declaration node.
	 */
	public SNPropertyDeclaration() {
	}
	
	/**
	 * Creates a view for given resource.
	 * @param resource
	 */
	public SNPropertyDeclaration(final SNResource resource) {
		super(resource);
	}
	
	//-----------------------------------------------------

	/**
	 * Returns the property declared by this property declaration.
	 * @return The property.
	 */
	public SNProperty getProperty() {
		SemanticNode node = getSingleClient(Aras.DECLARES_PROPERTY);
		if (node != null){
			return node.asResource().asProperty();
		} else {
			return null;
		}
	}
	
	/**
	 * Set the property declared by this property declaration.
	 * @param property The property.
	 * @param context The context.
	 */
	public void setProperty(final SemanticNode property, final Context context) {
		if (!Infra.equals(getProperty(), property)){
			removeAssocs(Aras.DECLARES_PROPERTY);
			Association.create(this, Aras.DECLARES_PROPERTY, property, context);
		}
	}
	
	// -----------------------------------------------------
	
	public SNClass getTypeConstraint() {
		SemanticNode node = getSingleClient(Aras.HAS_TYPE_CONSTRAINT);
		if (node != null){
			return node.asResource().asClass();
		} else {
			return null;
		}
	}

	public void setTypeConstraint(final ResourceID type, Context context) {
		if (!Infra.equals(getTypeConstraint(), type)){
			removeAssocs(Aras.HAS_TYPE_CONSTRAINT);
			Association.create(this, Aras.HAS_TYPE_CONSTRAINT, type, context);
		}
	}
	
	public SNScalar getCardinality(){
		throw new NotYetImplementedException();
	}
	
	//-----------------------------------------------------
	
	public int compareTo(final SNPropertyDeclaration other) {
		if (this.getProperty() == null){
			return 1;
		} else if (other.getProperty() == null){
			return -1;
		} else {
			return Infra.compare(this.getProperty().getQualifiedName(), other.getProperty().getQualifiedName());
		}
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		if (getProperty() != null){
			sb.append(getProperty().getQualifiedName().toQName());
		}
		sb.append(" (");
		if (getTypeConstraint() != null){
			sb.append(getTypeConstraint().getQualifiedName().toQName());
		}
		sb.append(")");
		return sb.toString();
	}
	
	//-----------------------------------------------------
	
	/**
	 * Removes all associations with given predicate.
	 * @param predicate The predicate.
	 */
	protected void removeAssocs(final ResourceID predicate){
		Set<Association> assocs = getAssociations(predicate);
		for (Association assoc : assocs) {
			revoke(assoc);
		}
	}
	
	private SemanticNode getSingleClient(final ResourceID predicate){
		Association association = getSingleAssociation(predicate);
		if (association != null){
			return association.getClient();
		} else {
			return null;
		}
	}
	
}
