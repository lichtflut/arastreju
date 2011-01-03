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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;

/**
 * View for a class resource.
 * 
 * Created: 23.01.2008
 *
 * @author Oliver Tigges
 */
public class SNClass extends ResourceView {
	
	public final static SNClass ROOT_CLASS = new SNClass();
	
	
	//------------------------------------------------------
	
	/**
	 * Creates a new class resource.
	 */
	public SNClass() {
	}
	
	/**
	 * Create a new view for given resource. 
	 */
	public SNClass(final SNResource resource) {
		super(resource);
	}
	
	//-- SUPER CONCEPTS -----------------------------------
	
	public Set<SNClass> getSuperClasses() {
		final Set<SNClass> allSuperClasses = new HashSet<SNClass>();
		Set<Association> extensions = getAssociations(RDFS.SUB_CLASS_OF);
		for (Association current : extensions) {
			SNClass directSuperClass = current.getClient().asResource().asClass();
			allSuperClasses.add(directSuperClass);
			allSuperClasses.addAll(directSuperClass.getSuperClasses());
		}
		return allSuperClasses;
	}
	
	public Set<SNClass> getDirectSuperClasses() {
		final Set<SNClass> superClasses = new HashSet<SNClass>();
		for (Association current : getAssociations(RDFS.SUB_CLASS_OF)) {
			final SNClass directImplementedClass = current.getClient().asResource().asClass();
			superClasses.add(directImplementedClass);
		}
		return superClasses;
	}
	
	public boolean isSpecializationOf(final ResourceID other) {
		return other.references(this) || getSuperClasses().contains(other);
	}
	
	//-- INSTANCES ----------------------------------------
	
	public SNEntity createInstance(Context context){
		SNEntity instance = new SNEntity();
		instance.setName("New" + getName());
		Association.create(instance, RDF.TYPE, this, context);
		return instance;
	}
	
	public SNEntity createInstance(final Context context, final QualifiedName qn){
		final SNEntity instance = new SNEntity();
		instance.setName(qn.getSimpleName());
		instance.setNamespace(qn.getNamespace());
		Association.create(instance, RDF.TYPE, this, context);
		return instance;
	}
	
	public boolean isInstance(final SNEntity individual){
		return individual.isInstanceOf(this);
	}
	
	// -- INTENSIONS --------------------------------------
	
	public List<SNTerm> getTerms(){
		Set<Association> intensions = getAssociations(RDFS.LABEL);
		List<SNTerm> terms = new ArrayList<SNTerm>(intensions.size());
		for (Association assoc : intensions) {
			terms.add(assoc.getClient().asValue().asTerm());
		}
		return terms;
	}
	
	// -- PROPRTIES ---------------------------------------
	
	/**
	 * Adds the given property declaration to this class.
	 * Assures that property declaration will be in same namespace as this classifier.
	 */
	public void addPropertyDeclaration(SNPropertyDeclaration decl, Context ctx){
		decl.setNamespace(getNamespace());
		Association.create(this, Aras.HAS_PROPERTY_DECL, decl, ctx);
	}
	
	/**
	 * Collects all property declarations defined by this classifier and it's super classifiers.
	 * @return The list of all property declarations.
	 */
	public List<SNPropertyDeclaration> getPropertyDeclarations(){
		Set<SNPropertyDeclaration> result = new HashSet<SNPropertyDeclaration>();
		for (SNClass superCl : getSuperClasses()) {
			result.addAll(superCl.getDeclaredPropertyDeclarations());
		}
		result.addAll(getDeclaredPropertyDeclarations());
		List<SNPropertyDeclaration> list = new ArrayList<SNPropertyDeclaration>(result);
		Collections.sort(list);
		return list;
	}
	
	/**
	 * Returns only the property declarations declared for this classifier, not the inherited.
	 * @return The list of property declarations.
	 */
	public List<SNPropertyDeclaration> getDeclaredPropertyDeclarations(){
		List<SNPropertyDeclaration> result = new ArrayList<SNPropertyDeclaration>();
		Set<Association> assocs = getAssociations(Aras.HAS_PROPERTY_DECL);
		for (Association current : assocs) {
			result.add(current.getClient().asResource().asPropertyDeclaration());
		}
		Collections.sort(result);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.model.semantic.ResourceClassifier#removePropertyDeclaration(org.arastreju.api.model.semantic.SNPropertyDeclaration)
	 */
	public void removePropertyDeclaration(SNPropertyDeclaration decl) {
		Set<Association> assocs = getAssociations(Aras.HAS_PROPERTY_DECL);
		Association toBeRemoved = null;
		for (Association current : assocs) {
			if (decl.equals(current.getClient())){
				toBeRemoved = current;
				break;
			}
		}
		revoke(toBeRemoved);
	}
	
}
