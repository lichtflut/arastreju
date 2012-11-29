package org.arastreju.sge.model.associations;

import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.StatementMetaInfo;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  A half statement is a statement without a subject.
 * </p>
 *
 * <p>
 *  Created 19.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class HalfStatement {

    protected final ResourceID predicate;
    protected final SemanticNode object;

    private final StatementMetaInfo metaInfo;

    private final int hash;

    // ----------------------------------------------------

    public static HalfStatement from(Statement stmt) {
        return new HalfStatement(stmt.getPredicate(), stmt.getObject());
    }

    // ----------------------------------------------------

    /**
     * Creates a new Statement.
     * @param predicate The predicate.
     * @param object The object.
     */
    public HalfStatement(final ResourceID predicate, final SemanticNode object) {
        this.predicate = predicate;
        this.object = object;
        metaInfo = new StatementMetaInfo();
        hash = calculateHash();
    }

    // -----------------------------------------------------

    public ResourceID getPredicate() {
        return predicate;
    }

    public SemanticNode getObject() {
        return object;
    }

    // ----------------------------------------------------

    public Statement toFullStatement(ResourceID subject) {
        return new DetachedStatement(subject, predicate, object, metaInfo);
    }

    public Statement toFullStatement(ResourceID subject, StatementMetaInfo smi) {
        return new DetachedStatement(subject, predicate, object, smi);
    }

    // -----------------------------------------------------

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof HalfStatement)) {
            return false;
        }
        if (hash != obj.hashCode()) {
            return false;
        }
        final HalfStatement other = (HalfStatement) obj;
        if (!object.equals(other.getObject())){
            return false;
        }
        if (!predicate.equals(other.getPredicate())){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(" {*} ");
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
        return result;
    }
}
