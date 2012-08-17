package org.arastreju.bindings.rdb.tx;

import java.sql.Connection;
import java.sql.SQLException;

import org.arastreju.bindings.rdb.jdbc.ConnectionWraper;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.persistence.TransactionControl;
import org.arastreju.sge.persistence.TxProvider;

public class JdbcTxProvider extends TxProvider{
	
	private final ConnectionWraper cw;
	
	public JdbcTxProvider(ConnectionWraper cw){
		this.cw=cw;
	}
	
	@Override
	protected TransactionControl newTx() {
		try {
			final Connection con = cw.getConnection();
			return new JdbcTransactionControl(con);
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE, e.getMessage());
		}
	}

}
