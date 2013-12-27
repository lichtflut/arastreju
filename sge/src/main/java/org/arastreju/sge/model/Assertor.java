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
import org.arastreju.sge.model.associations.DefaultStatementMetaInfo;
import org.arastreju.sge.model.associations.StatementMetaInfo;
import org.arastreju.sge.model.associations.TimedStatementMetaInfo;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.StatementOrigin;

import java.util.Date;

/**
 * <p>
 *  Builder for statements.
 * </p>
 *
 * <p>
 * 	Created Dec 18, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class Assertor {

    public static final Context[] NO_CTX = new Context[0];

    private Context[] contexts = NO_CTX;
    private StatementOrigin origin = StatementOrigin.ASSERTED;

    private Date timetamp;
    private ResourceID subject;
    private ResourceID predicate;
    private SemanticNode object;
    private Date validFrom;
    private Date validUntil;

    //-----------------------------------------------------

    public static Assertor start(ResourceID subject, ResourceID predicate, SemanticNode object) {
        return new Assertor().statement(subject, predicate, object);
    }

    //-----------------------------------------------------

    public Assertor statement(ResourceID subject, ResourceID predicate, SemanticNode object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
        return this;
    }

    public Assertor context(Context... contexts) {
        this.contexts = contexts;
        return this;
    }

    public Assertor timestamp(long tsp) {
        return timestamp(toDate(tsp));
    }

    public Assertor timestamp(Date tsp) {
        this.timetamp = tsp;
        return this;
    }

    public Assertor validFrom(Long validFrom) {
        this.validFrom = toDate(validFrom);
        return this;
    }

    public Assertor validUntil(Long validUntil) {
        this.validUntil = toDate(validUntil);
        return this;
    }

    public Assertor validFrom(Date validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public Assertor validUntil(Date validUntil) {
        this.validUntil = validUntil;
        return this;
    }

    //-----------------------------------------------------

    public Statement build() {
        return new DetachedStatement(subject, predicate, object, createMetaInfo());
    }

    StatementMetaInfo createMetaInfo() {
        Date tsp = timetamp != null ? timetamp : new Date();
        if (validFrom != null || validUntil != null) {
            return new TimedStatementMetaInfo(contexts, tsp, origin, validFrom, validUntil);
        }
        else return new DefaultStatementMetaInfo(contexts, tsp, origin);
    }

    //-----------------------------------------------------

    private Date toDate(Long tsp) {
        if (tsp == null || tsp == 0) {
            return null;
        } else {
            return new Date(tsp);
        }
    }

}
