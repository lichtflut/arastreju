/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb;

import java.sql.Connection;
import java.sql.SQLException;

import org.arastreju.bindings.rdb.jdbc.ConnectionWraper;
import org.arastreju.bindings.rdb.tx.JdbcTxProvider;
import org.arastreju.sge.persistence.TxProvider;
import org.arastreju.sge.spi.abstracts.AbstractConversationContext;

/**
 * <p>
 * RRdb specific extension of AbstractConversationContext.
 * </p>
 * 
 * <p>
 * Created 23.07.2012
 * </p>
 * 
 * @author Raphael Esterle
 */
public class RdbConversationContext extends AbstractConversationContext {

	private final JdbcTxProvider txProvider;
	private final Cache cache;
	private final ConnectionWraper cw;
	// ----------------------------------------------------

	public RdbConversationContext(ConnectionWraper cw) {
		this.txProvider = new JdbcTxProvider(cw);
		cache = new Cache();
		this.cw = cw;
	}

	// ----------------------------------------------------
	
	public JdbcTxProvider getJdbcTxProvider(){
		return txProvider;
	}
	
	@Override
	public TxProvider getTxProvider() {
		return txProvider;
	}

	@Override
	protected void clearCaches() {
		cache.clear();
	}

	public String getTable() {
		return cw.getTable();
	}

	public Cache getCache() {
		return cache;
	}
	
	public Connection getConnection() throws SQLException{
			return cw.getConnection();
	}
}
