/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.arastreju.sge.context.Context;

/**
 * <p>
 *  Meta information about a statement.
 * </p>
 *
 * <p>
 * 	Created May 4, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class StatementMetaInfo implements Serializable {
	
	public static final Context[] NO_CTX = new Context[0];

	private final Date timestamp;
	private final Context[] contexts;
	
	// ----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param timestamp
	 */
	public StatementMetaInfo(Context[] contexts, Date timestamp) {
		this.timestamp = timestamp;
		
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
	
	/**
	 * @param contexts
	 */
	public StatementMetaInfo(Context[] contexts) {
		this(contexts, new Date());
	}
	
	// ----------------------------------------------------

	/**
	 * @return the contexts
	 */
	public Context[] getContexts() {
		return contexts;
	}
	
	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
}
