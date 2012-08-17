/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb;

import java.sql.Connection;
import java.sql.SQLException;

import org.arastreju.bindings.rdb.jdbc.ConnectionWraper;
import org.arastreju.bindings.rdb.tx.JdbcTxProvider;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
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

	private final TxProvider txProvider;
	private final String table;
	private final Cache cache;
	private final ConnectionWraper cw;
	// ----------------------------------------------------

	public RdbConversationContext(ConnectionWraper cw, String table) {
		this.txProvider = new JdbcTxProvider(cw);
		this.table = table;
		cache = new Cache();
		this.cw = cw;
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
		try {
			return cw.getConnection();
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE, e.getMessage());
		}
	}
}
