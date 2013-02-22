package org.arastreju.bindings.memory.storage;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.bindings.memory.tx.MemTransactionProvider;
import org.arastreju.sge.model.associations.AttachedAssociationKeeper;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.AssociationResolver;
import org.arastreju.sge.spi.AssociationWriter;
import org.arastreju.sge.spi.GraphDataStore;
import org.arastreju.sge.spi.WorkingContext;

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
public class MemStorage implements GraphDataStore {

    private final Map<QualifiedName, StoredResource> store = new HashMap<QualifiedName, StoredResource>();

    // ----------------------------------------------------

    @Override
    public AttachedAssociationKeeper find(QualifiedName qn) {
        StoredResource storedResource = store.get(qn);
        if (storedResource != null) {
            return new AttachedAssociationKeeper(qn, storedResource.getId(), storedResource.getStatements());
        } else {
            return null;
        }
    }

    @Override
    public AttachedAssociationKeeper create(QualifiedName qn) {
        StoredResource storedResource = new StoredResource(qn);
        store.put(qn, storedResource);
        return new AttachedAssociationKeeper(qn, storedResource.getId());
    }

    @Override
    public void remove(QualifiedName qn) {
        store.remove(qn);
    }

    @Override
    public AssociationResolver createAssociationResolver(WorkingContext ctx) {
        throw new NotYetImplementedException();
    }

    @Override
    public AssociationWriter crateAssociationWriter(WorkingContext ctx) {
        throw new NotYetImplementedException();
    }

    @Override
    public TxProvider getTxProvider() {
        return new MemTransactionProvider();
    }
}
