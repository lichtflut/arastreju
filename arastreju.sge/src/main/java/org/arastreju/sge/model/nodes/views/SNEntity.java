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
package org.arastreju.sge.model.nodes.views;

import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  View on all entities (instances of classes).
 * </p>
 * 
 * Created: 12.09.2008
 * 
 * @author Oliver Tigges
 */
public class SNEntity extends ResourceView {
	
	public SNEntity() {
		super(new SNResource());
	}
	
	public SNEntity(final QualifiedName qn) {
		super(new SNResource(qn));
	}
	
	/**
	 * Create a new entity view on given resource.
	 * @param resource The resource to be wrapped by view.
	 */
	public SNEntity(final ResourceNode resource) {
		super(resource);
	}

	//------------------------------------------------------

	/**
	 * Get all direct classes, i.e. with asserted rdf:type, not inferred.
	 * @return A set with the direct classes.
	 */
	public Set<SNClass> getDirectClasses() {
		Set<SNClass> result = new HashSet<SNClass>();
		for(Statement assoc: getAssociations(RDF.TYPE)){
			result.add(assoc.getObject().asResource().asClass());
		}
		return result;
	}
	
	/**
	 * Check if this entity is member of at least one class.
	 * @return true, if this entity has a class.
	 */
	public boolean hasClass(){
		return !getAssociations(RDF.TYPE).isEmpty();
	}
	
	public boolean isInstanceOf(final ResourceID type) {
		for (SNClass clazz : getDirectClasses()) {
			if (clazz.isSpecializationOf(type)){
				return true;
			}
		}
		return false;
	}

}