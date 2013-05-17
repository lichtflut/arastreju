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
