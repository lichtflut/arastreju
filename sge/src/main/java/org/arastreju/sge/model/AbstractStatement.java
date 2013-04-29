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
package org.arastreju.sge.model;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  Abstract base for {@link Statement}s.
 * </p>
 *
 * <p>
 * 	Created May 5, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class AbstractStatement implements Statement {
	
	protected final ResourceID subject;
	protected final ResourceID predicate;
	protected final SemanticNode object;
	
	private final StatementMetaInfo metaInfo;
	
	private final int hash;
	
	// -----------------------------------------------------

	/**
	 * Creates a new Statement.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @param object The object.
	 * @param contexts The contexts of this statement.
	 */
	public AbstractStatement(final ResourceID subject, final ResourceID predicate,
			final SemanticNode object, final Context... contexts) {
		this(subject, predicate, object, new StatementMetaInfo(contexts));
	}
	
	/**
	 * Creates a new Statement.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @param object The object.
	 * @param metaInfo The statement meta information.
	 */
	public AbstractStatement(final ResourceID subject, final ResourceID predicate,
			final SemanticNode object, final StatementMetaInfo metaInfo) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		this.metaInfo = metaInfo;
		hash = calculateHash();
	}
	
	// -----------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public ResourceID getSubject() {
		return subject;
	}

	/**
	 * {@inheritDoc}
	 */
	public ResourceID getPredicate() {
		return predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	public SemanticNode getObject() {
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	public Context[] getContexts() {
		return metaInfo.getContexts();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public StatementMetaInfo getMetaInfo() {
		return metaInfo;
	}
	
	// -----------------------------------------------------

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Statement)) {
			return false;
		}
		if (hash != obj.hashCode()) {
			return false;
		}
		final Statement other = (Statement) obj;
		if (!object.equals(other.getObject())){
			return false;
		}
		if (!subject.equals(other.getSubject())){
			return false;
		}
		if (!predicate.equals(other.getPredicate())){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder(subject.toString());
		sb.append(" " + predicate + " ");
		sb.append(object);
		return sb.toString();
	}
	
	// ----------------------------------------------------
	
	private int calculateHash() {
		final int prime = 31;
		int result = 1;
		result = prime * result + object.hashCode();
		result = prime * result + predicate.hashCode();
		result = prime * result + subject.hashCode();
		return result;
	}

}