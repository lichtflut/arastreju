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
package org.arastreju.bindings.memory.storage;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.StatementMetaInfo;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  A stored, detached statment.
 * </p>
 *
 * <p>
 *  Created May 17, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class StoredStatement {

    private final QualifiedName predicate;

    private final Object object;

    private final ElementaryDataType dataType;

    private final StatementMetaInfo metaInfo;

    // ----------------------------------------------------

    public StoredStatement(Statement stmt) {
        this.predicate = stmt.getPredicate().getQualifiedName();
        this.metaInfo = stmt.getMetaInfo();
        if (stmt.getObject().isResourceNode()) {
            this.object = stmt.getObject().asResource().getQualifiedName();
            this.dataType = ElementaryDataType.RESOURCE;
        } else {
            ValueNode valueNode = stmt.getObject().asValue();
            this.object = valueNode.getValue();
            this.dataType = valueNode.getDataType();
        }
    }

    // ----------------------------------------------------

    public Context[] getContexts() {
        return metaInfo.getContexts();
    }

    public Statement load(ResourceID subject) {
        return new DetachedStatement(subject, SimpleResourceID.from(predicate), getObject(), metaInfo);
    }

    // ----------------------------------------------------

    protected SemanticNode getObject() {
        if (ElementaryDataType.RESOURCE == dataType) {
            return SimpleResourceID.from((QualifiedName) object);
        } else {
            return new SNValue(dataType, object);
        }
    }

}
