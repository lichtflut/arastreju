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
package org.arastreju.sge.model.associations;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.StatementOrigin;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

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
     * @param timestamp The timestamp of the creation of the statement.
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
	 * @param timestamp The timestamp of the creation of the statement.
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
