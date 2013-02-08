package org.arastreju.bindings.memory.tx;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.sge.persistence.PseudoTransaction;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.persistence.TxProvider;

/**
 * <p>
 *  In-Memory specific TX provider.
 * </p>
 *
 * <p>
 *  Created 20.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemTransactionProvider extends TxProvider {

    @Override
    protected TransactionControl newTx() {
        return new PseudoTransaction();
    }

}
