/**
 * 
 */
package org.arastreju.bindings.rdb;

import org.arastreju.bindings.rdb.tx.JdbcTxProvider;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.abstracts.AbstractConversationContext;

/**
 * @author raphael
 */
public class RdbConversationContext extends AbstractConversationContext {
	
	private RdbConnectionProvider connectionProvider;

	public RdbConversationContext(RdbConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}
	

	@Override
	public TxProvider getTxProvider() {
		return new JdbcTxProvider();
	}

	@Override
	protected void clearCaches() {
	}
	
	public RdbConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

}
