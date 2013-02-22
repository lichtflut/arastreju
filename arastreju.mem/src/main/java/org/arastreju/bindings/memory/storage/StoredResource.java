package org.arastreju.bindings.memory.storage;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.PhysicalNodeID;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  Represents a persistent resource in the storage.
 * </p>
 *
 * <p>
 *  Created 26.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class StoredResource {

    private PhysicalNodeID id;

    private final QualifiedName qn;

    private final Set<Statement> statements = new HashSet<Statement>();

    // ----------------------------------------------------

    public StoredResource(QualifiedName qn) {
        this.qn = qn;
    }

    // ----------------------------------------------------

    public PhysicalNodeID getId() {
        return id;
    }

    public QualifiedName getQn() {
        return qn;
    }

    public Set<Statement> getStatements() {
        return statements;
    }

    public void addAssociation(Statement stmt) {
        statements.add(stmt);
    }

    public void remove(Statement stmt) {
        statements.remove(stmt);
    }
}
