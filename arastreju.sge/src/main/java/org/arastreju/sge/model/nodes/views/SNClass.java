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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
			SNClass directSuperClass = current.getObject().asResource().asClass();
			allSuperClasses.add(directSuperClass);
			allSuperClasses.addAll(directSuperClass.getSuperClasses());
		}
		return allSuperClasses;
	}
	
	public Set<SNClass> getDirectSuperClasses() {
		final Set<SNClass> superClasses = new HashSet<SNClass>();
		for (Association current : getAssociations(RDFS.SUB_CLASS_OF)) {
			final SNClass directImplementedClass = current.getObject().asResource().asClass();
			superClasses.add(directImplementedClass);
		}
		return superClasses;
	}
	
	public boolean isSpecializationOf(final ResourceID other) {
		return other.references(this) || getSuperClasses().contains(other);
	}
	
	//-- INSTANCES ----------------------------------------
	
	public SNEntity createInstance(final Context... contexts){
		final SNEntity instance = new SNEntity();
		Association.create(instance, RDF.TYPE, this, contexts);
		return instance;
	}
	
	public SNEntity createInstance(final QualifiedName qn, final Context... contexts){
		final SNEntity instance = new SNEntity();
		instance.setName(qn.getSimpleName());
		instance.setNamespace(qn.getNamespace());
		Association.create(instance, RDF.TYPE, this, contexts);
		return instance;
	}
	
	public boolean isInstance(final SNEntity individual){
		return individual.isInstanceOf(this);
	}
	
	// -- INTENSIONS --------------------------------------
	
	public List<SNText> getTerms(){
		Set<Association> intensions = getAssociations(RDFS.LABEL);
		List<SNText> terms = new ArrayList<SNText>(intensions.size());
		for (Association assoc : intensions) {
			terms.add(assoc.getObject().asValue().asText());
		}
		return terms;
	}
	
}
