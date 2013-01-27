package org.arastreju.bindings.memory.storage;

import org.arastreju.bindings.memory.keepers.MemAssocKeeper;
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
public class MemStorage implements GraphDataStore<MemAssocKeeper> {

    private final Map<QualifiedName, StoredResource> store = new HashMap<QualifiedName, StoredResource>();

    // ----------------------------------------------------

    @Override
    public MemAssocKeeper find(QualifiedName qn) {
        StoredResource storedResource = store.get(qn);
        if (storedResource != null) {
            return new MemAssocKeeper(storedResource.getStatements());
        } else {
            return null;
        }
    }

    @Override
    public MemAssocKeeper create(QualifiedName qn) {
        store.put(qn, new StoredResource(qn));
        return new MemAssocKeeper(qn);
    }

}
