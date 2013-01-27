package org.arastreju.bindings.memory.storage;

import org.arastreju.bindings.memory.keepers.MemAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.spi.GraphDataStore;

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
            return new MemAssociationKeeper(storedResource.getStatements());
        } else {
            return null;
        }
    }

    @Override
    public MemAssociationKeeper create(QualifiedName qn) {
        store.put(qn, new StoredResource(qn));
        return new MemAssociationKeeper(qn);
    }

}
