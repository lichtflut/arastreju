/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.rdb.tx;

import java.sql.Connection;
import java.sql.SQLException;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.persistence.TransactionControl;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

/**
 * <p>
 * Jdbc specific implementation of the TransactionControl.
 * </p>
 * 
 * <p>
 * Created 16.08.2012
 * </p>
 * 
 * @author Raphael Esterle
 */
public class JdbcTransactionControl implements TransactionControl {

	private final Connection con;
	private boolean succsess = false;

	public JdbcTransactionControl(Connection con) {
		this.con = con;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void success() {
		succsess = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail() {
		succsess = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finish() {
		if (!succsess)
			rollback();
		else
			commit();
		try {
			con.close();
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(
					ErrorCodes.GENERAL_CONSISTENCY_FAILURE, "JDBC (close): "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		try {
			con.commit();
		} catch (SQLException e) {
			fail();
			throw new ArastrejuRuntimeException(
					ErrorCodes.GENERAL_CONSISTENCY_FAILURE, "JDBC (commit): "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		try {
			con.rollback();
		} catch (SQLException e) {
			throw new ArastrejuRuntimeException(
					ErrorCodes.GENERAL_CONSISTENCY_FAILURE, "JDBC (rollback): "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() {
		throw new NotYetImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive() {
		return null != con;
	}

	protected void assertTxActive() {
		if (!isActive()) {
			throw new IllegalStateException(
					"Transaction has already been closed.");
		}
	}
}
