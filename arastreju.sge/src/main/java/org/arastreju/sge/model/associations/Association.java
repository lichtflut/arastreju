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
package org.arastreju.sge.model.associations;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

import de.lichtflut.infra.Infra;

/**
 * <p>
 * 	Association between two {@link SemanticNode}s with a predicate, corresponds to a
 * 	triple in RDF. 
 *  Immutable Object!
 * </p>
 * 
 * <p>
 * 	Created: 04.11.2007
 * </p>
 *
 * @author Oliver Tigges
 * 
 */
public class Association {
	
	private final Context context;
	
	private final ResourceNode supplier;
	
	private final SemanticNode client;
	
	private final ResourceID predicate;
	
	// -- STATIC METHODS ----------------------------------
	
	/**
	 * Creates a new Association based on given data.
	 */
	public static Association create(final ResourceNode subject, final ResourceID predicate, final SemanticNode object, final Context context){
		Association assoc =  new Association(subject, predicate, object, context);
		if (!subject.hasAssociation(assoc)){
			subject.addToAssociations(assoc);
			return assoc;
		} else {
			return assoc;
		}
	}
	
	//-- CONSTRUCTORS -------------------------------------
	
	/**
	 * Private constructor for new Associations. Use the static create() methods.
	 * @param supplier The supplier of the association.
	 * @param predicate The predicate.
	 * @param client The client.
	 * @param context The context.
	 */
	private Association(final SemanticNode supplier, final ResourceID predicate, final SemanticNode client, final Context context) {
		if (supplier == null || client == null || predicate == null){
			throw new IllegalArgumentException("none of the three triple elements may be null:" +
					" supplier = " + supplier + "; client = " + client + "; predicate = " + predicate);
		}
		this.context = context;
		this.supplier = supplier.asResource();
		this.client = client;
		this.predicate = predicate;
	}
	
	//-----------------------------------------------------
	
	public ResourceNode getSupplier() {
		return supplier;
	}

	public SemanticNode getClient() {
		return client;
	}

	public ResourceID getPredicate() {
		return predicate;
	}
	
	public Context getContext() {
		return context;
	}
	
	/**
	 * Checks if all elements of this association are attached.
	 * @return true if attached.
	 */
	public boolean isAttached(){
		return supplier.isAttached() && client.isAttached() && predicate.isAttached();
	}
	
	// -----------------------------------------------------
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Association){
			Association other = (Association) obj;
			if (!Infra.equals(client, other.getClient())){
				return false;
			}
			if (!Infra.equals(supplier, other.getSupplier())){
				return false;
			}
			if (!Infra.equals(predicate, other.getPredicate())){
				return false;
			}
			if (!Infra.equals(context, other.getContext())){
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return supplier.hashCode() + client.hashCode(); 
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(supplier.toString());
		sb.append(" " + predicate + " ");
		sb.append(client);
		return sb.toString();
	}

}
