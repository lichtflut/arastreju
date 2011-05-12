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

import java.io.Serializable;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.AbstractStatement;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 * 	Association between two {@link SemanticNode}s with a predicate, corresponds to a
 * 	triple in RDF. 
 *  Immutable Object!
 * </p>
 * 
 * <p>
 * 	An Association is a {@link Statement} that can be attached to a semantic graph.
 * </p>
 * 
 * <p>
 * 	Created: 04.11.2007
 * </p>
 *
 * @author Oliver Tigges
 * 
 */
public class Association extends AbstractStatement implements Serializable {
	
	// -- STATIC METHODS ----------------------------------
	
	/**
	 * Creates a new Association based on given data.
	 */
	public static Association create(final ResourceNode subject, final ResourceID predicate, final SemanticNode object, final Context... contexts){
		Association assoc =  new Association(subject, predicate, object, contexts);
		if (!subject.hasAssociation(assoc)){
			subject.addToAssociations(assoc);
			return assoc;
		} else {
			return assoc;
		}
	}
	
	//-- CONSTRUCTORS -------------------------------------
	
	/**
	 * Package protected constructor for new Associations. Use the static create() methods.
	 * @param subject The supplier of the association.
	 * @param predicate The predicate.
	 * @param object The client.
	 * @param context The context.
	 */
	Association(final ResourceNode subject, final ResourceID predicate, final SemanticNode object, final Context... contexts) {
		if (subject == null || object == null || predicate == null){
			throw new IllegalArgumentException("none of the three triple elements may be null:" +
					" supplier = " + subject + "; client = " + object + "; predicate = " + predicate);
		}
		this.subject = subject;
		this.object = object;
		this.predicate = predicate;
		setContexts(contexts);
	}
	
	//-----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.associations.Statement#getSubject()
	 */
	public ResourceNode getSubject() {
		return (ResourceNode) subject;
	}
	
	/**
	 * Checks if all elements of this association are attached.
	 * @return true if attached.
	 */
	public boolean isAttached(){
		return subject.isAttached() && object.isAttached() && predicate.isAttached();
	}
	
	// -----------------------------------------------------
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(subject.toString());
		sb.append(" " + predicate + " ");
		sb.append(object);
		return sb.toString();
	}
	
	// -----------------------------------------------------
	
	/**
	 * @deprecated Use getSubject() instead.
	 */
	public ResourceNode getSupplier() {
		return getSubject();
	}
	
	/**
	 * @deprecated Use getObject() instead.
	 */
	public SemanticNode getClient() {
		return object;
	}
	
	/**
	 * @deprecated Use getContexts() instead.
	 */
	public Context getContext() {
		final Context[] contexts = super.getContexts();
		if (contexts == null || contexts.length == 0) {
			return null;
		} else if (contexts.length == 1) {
			return contexts[0];
		} else {
			throw new IllegalStateException("Called getContext() when there where " + contexts.length + " contexts.");
		}
	}

}
