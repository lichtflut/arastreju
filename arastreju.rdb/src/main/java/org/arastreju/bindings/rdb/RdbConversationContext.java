/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb;

import java.sql.Connection;

import org.arastreju.bindings.rdb.tx.JdbcTxProvider;
import org.arastreju.sge.persistence.TransactionControl;
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

	private TxProvider txProvider;
	private String table;
	private Cache cache;
	private Connection con;
	// ----------------------------------------------------

	public RdbConversationContext(Connection con, String table) {
		this.txProvider = new JdbcTxProvider(con);
		this.table = table;
		cache = new Cache();
		this.con = con;
	}

	// ----------------------------------------------------

	@Override
	public TxProvider getTxProvider() {
		return txProvider;
	}

	@Override
	protected void clearCaches() {
	}

	public String getTable() {
		return table;
	}

	public Cache getCache() {
		return cache;
	}
	
	public Connection getConnection(){
		return con;
	}
}
