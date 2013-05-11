package org.arastreju.sge.spi.impl;

import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.NodeKeyTable;
import org.arastreju.sge.spi.PhysicalNodeID;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  Cache for mappings from qualified names to physical IDs.
 * </p>
 *
 * <p>
 *  Created May 10, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public class NodeKeyTableTxCache<T extends PhysicalNodeID> implements NodeKeyTable<T> {

    private final Map<QualifiedName, T> added = new HashMap<QualifiedName, T>();

    private final Set<QualifiedName> removed = new HashSet<QualifiedName>();

    // ----------------------------------------------------

    @Override
    public T lookup(QualifiedName qualifiedName) {
        return added.get(qualifiedName);
    }

    @Override
    public void put(QualifiedName qn, T physicalID) {
        added.put(qn, physicalID);
    }

    @Override
    public void remove(QualifiedName qn) {
        if (added.containsKey(qn)) {
            added.remove(qn);
        } else {
            removed.add(qn);
        }
    }

    // ----------------------------------------------------

    public Map<QualifiedName, T> getAddedEntries() {
        return added;
    }

    public Set<QualifiedName> getRemovedEntries() {
        return removed;
    }

    public void clear() {
        added.clear();
        removed.clear();
    }

    // ----------------------------------------------------

    @Override
    public void shutdown() throws IOException {
        clear();
    }

}
