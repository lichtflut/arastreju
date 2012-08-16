package org.arastreju.bindings.rdb.tx;

import java.sql.Connection;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.persistence.TxProvider;

public class JdbcTxProvider extends TxProvider{
	
	private final Connection con;
	
	public JdbcTxProvider(Connection con){
		this.con=con;
	}
	
	@Override
	protected TransactionControl newTx() {
		return new JdbcTransactionControl(con);
	}

}
