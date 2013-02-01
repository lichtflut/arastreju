package org.arastreju.bindings.memory.storage;

import org.arastreju.bindings.memory.keepers.MemAssociationKeeper;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.GraphDataStore;
import org.arastreju.sge.spi.PhysicalNodeID;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  The in memory database for resource nodes.
 * </p>
 *
 * <p>
 *  Created 25.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemStorage implements GraphDataStore<MemAssociationKeeper> {

    private final Map<QualifiedName, StoredResource> store = new HashMap<QualifiedName, StoredResource>();

    // ----------------------------------------------------

    @Override
    public MemAssociationKeeper find(QualifiedName qn) {
        StoredResource storedResource = store.get(qn);
        if (storedResource != null) {
            return new MemAssociationKeeper(qn, storedResource.getId(), storedResource.getStatements());
        } else {
            return null;
        }
    }

    @Override
    public MemAssociationKeeper create(QualifiedName qn) {
        StoredResource storedResource = new StoredResource(qn);
        store.put(qn, storedResource);
        return new MemAssociationKeeper(qn, storedResource.getId());
    }

    @Override
    public void remove(QualifiedName qn) {
        store.remove(qn);
    }

    @Override
    public boolean addAssociation(PhysicalNodeID id, Statement assoc) {
        QualifiedName qn = assoc.getSubject().getQualifiedName();
        StoredResource stored = store.get(qn);
        stored.addAssociation(assoc);
        return true;
    }
}
