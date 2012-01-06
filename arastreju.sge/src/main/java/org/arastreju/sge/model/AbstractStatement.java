/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.model;

import java.util.Arrays;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.SemanticNode;

import de.lichtflut.infra.Infra;

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
	
	public static final Context[] NO_CTX = new Context[0];

	protected ResourceID subject;
	protected ResourceID predicate;
	protected SemanticNode object;
	
	private Context[] contexts = NO_CTX;
	
	private boolean inferred;
	
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
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		setContexts(contexts);
	}
	
	/**
	 * Constructor.
	 */
	protected AbstractStatement() {
		super();
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
		return contexts;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isInferred() {
		return inferred;
	}
	
	// -----------------------------------------------------

	protected Statement setInferred(boolean inferred) {
		this.inferred = inferred;
		return this;
	}
	
	/**
	 * @param contexts the contexts to set
	 */
	protected void setContexts(final Context[] contexts) {
		if (contexts == null || (contexts.length == 1 && contexts[0] == null)) {
			this.contexts = NO_CTX;
		} else {
			for (Context ctx : contexts) {
				if (ctx == null) {
					throw new IllegalArgumentException("Null context not allowed!");
				}
			}
			this.contexts = Arrays.copyOf(contexts, contexts.length);
			Arrays.sort(this.contexts);
		}
	}

	// -----------------------------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Statement)) {
			return false;
		}
		final Statement other = (Statement) obj;
		if (!Infra.equals(subject, other.getSubject())){
			return false;
		}
		if (!Infra.equals(predicate, other.getPredicate())){
			return false;
		}
		if (!Infra.equals(object, other.getObject())){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(subject.toString());
		sb.append(" " + predicate + " ");
		sb.append(object);
		return sb.toString();
	}

}