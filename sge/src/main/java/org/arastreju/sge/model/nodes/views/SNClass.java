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
package org.arastreju.sge.model.nodes.views;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;

import java.util.HashSet;
import java.util.Set;

/**
 * View for a class resource.
 * 
 * Created: 23.01.2008
 *
 * @author Oliver Tigges
 */
public class SNClass extends ResourceView {

    public static SNClass from(SemanticNode node) {
        if (node instanceof SNClass) {
            return (SNClass) node;
        } else if (node instanceof ResourceNode) {
            return new SNClass((ResourceNode) node);
        } else if (node instanceof ResourceID) {
            return new SNClass(node.asResource());
        } else {
            return null;
        }
    }

    // ----------------------------------------------------

	/**
	 * Creates a new class resource.
	 */
	public SNClass() {
	}

	/**
	 * Create a new view for given resource.
	 */
	public SNClass(final ResourceNode resource) {
		super(resource);
	}

	//-- SUPER CONCEPTS -----------------------------------

	public Set<SNClass> getSuperClasses() {
		final Set<SNClass> allSuperClasses = new HashSet<SNClass>();
		Set<? extends Statement> extensions = getAssociations(RDFS.SUB_CLASS_OF);
		for (Statement current : extensions) {
			SNClass directSuperClass = SNClass.from(current.getObject());
			allSuperClasses.add(directSuperClass);
			allSuperClasses.addAll(directSuperClass.getSuperClasses());
		}
		return allSuperClasses;
	}

	public Set<SNClass> getDirectSuperClasses() {
		final Set<SNClass> superClasses = new HashSet<SNClass>();
		for (Statement current : SNOPS.associations(this, RDFS.SUB_CLASS_OF)) {
			final SNClass directImplementedClass = SNClass.from(current.getObject());
			superClasses.add(directImplementedClass);
		}
		return superClasses;
	}

	public boolean isSpecializationOf(final ResourceID other) {
		return other.equals(this) || getSuperClasses().contains(other);
	}

	//-- INSTANCES ----------------------------------------

	public SNEntity createInstance(){
		final SNEntity instance = new SNEntity();
		SNOPS.associate(instance, RDF.TYPE, this);
		return instance;
	}

	public SNEntity createInstance(final QualifiedName qn){
		final SNEntity instance = new SNEntity(qn);
		SNOPS.associate(instance, RDF.TYPE, this);
		return instance;
	}

	public boolean isInstance(final SNEntity individual){
		return individual.isInstanceOf(this);
	}

}
