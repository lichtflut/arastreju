package org.arastreju.bindings.memory.tx;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.persistence.TxProvider;

/**
 * <p>
 * DESCRITPION.
 * </p>
 * <p/>
 * <p>
 * Created 20.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class MemTransactionProvider extends TxProvider {

    @Override
    protected TransactionControl newTx() {
        throw new NotYetImplementedException();
    }

    @Override
    protected TransactionControl newSubTx(TransactionControl parent) {
        throw new NotYetImplementedException();
    }
}
