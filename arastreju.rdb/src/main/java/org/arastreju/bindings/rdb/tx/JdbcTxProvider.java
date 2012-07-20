package org.arastreju.bindings.rdb.tx;

import org.arastreju.sge.persistence.PseudoTransaction;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.persistence.TxProvider;

public class JdbcTxProvider extends TxProvider{

	@Override
	protected TransactionControl newTx() {
		return new PseudoTransaction();
	}

}
