/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.StatementOrigin;

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
public class StatementMetaInfo implements Serializable, Cloneable {
	
	public static final Context[] NO_CTX = new Context[0];

	private final Context[] contexts;
    private final Date timestamp;
    private StatementOrigin origin;
	
	// ----------------------------------------------------

    /**
     * Constructor.
     * @param contexts The contexts.
     * @param timestamp The timestamp of the creation of the statment.
     */
    public StatementMetaInfo(Context[] contexts, Date timestamp, StatementOrigin origin) {
        this.timestamp = timestamp;
        this.origin = origin;

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
	 * Constructor.
     * @param contexts The contexts.
	 * @param timestamp The timestamp of the creation of the statment.
	 */
	public StatementMetaInfo(Context[] contexts, Date timestamp) {
        this(contexts, timestamp, StatementOrigin.ASSERTED);
	}
	
	/**
     * Constructor.
	 * @param contexts The contexts.
	 */
	public StatementMetaInfo(Context[] contexts) {
		this(contexts, new Date());
	}

    public StatementMetaInfo() {
        this(null);
    }

    // ----------------------------------------------------

    public StatementMetaInfo infer() {
        try {
            StatementMetaInfo clone = (StatementMetaInfo) clone();
            clone.origin = StatementOrigin.INFERRED;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public StatementMetaInfo inherit() {
        try {
            StatementMetaInfo clone = (StatementMetaInfo) clone();
            clone.origin = StatementOrigin.INHERITED;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
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

    public StatementOrigin getOrigin() {
        return origin;
    }

    // ----------------------------------------------------


    @Override
    public String toString() {
        return "StatementMetaInfo{" +
                "timestamp=" + timestamp +
                ", contexts=" + (contexts == null ? null : Arrays.asList(contexts)) +
                ", origin=" + origin +
                '}';
    }
}
