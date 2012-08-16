/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.bindings.rdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Manages a pool of JDBC connections.
 * </p>
 * 
 * <p>
 * Created 23.07.2012
 * </p>
 * 
 * @author Raphael Esterle
 */

public class RdbConnectionProvider {

	private Logger logger = LoggerFactory
			.getLogger(RdbConnectionProvider.class);

	private final String driver;
	private final String user;
	private final String pass;
	private final String url;
	private final String table;

	private final int max_cons;

	private Vector<Connection> usedCons;
	private Vector<Connection> cons;

	// ----------------------------------------------------

	public RdbConnectionProvider(String driver, String user, String pass,
			String url, String table, int max_cons) {
		super();
		this.driver = driver;
		this.user = user;
		this.pass = pass;
		this.url = url;
		this.table = table;
		this.max_cons = max_cons;

		usedCons = new Vector<Connection>();
		cons = new Vector<Connection>();
	}

	// ----------------------------------------------------

	public Connection getConnection() {
		Connection con = null;
		if (usedSize() + poolSize() + 1 > max_cons) {
			logger.info("maximun is reached! " + getLogInfo());
			return con;
		} else if (cons.size() > 0) {
			con = cons.lastElement();
			cons.remove(con);
			usedCons.add(con);
			logger.debug("returned pooled connection " + getLogInfo());
			return con;
		}
		con = createCon();
		usedCons.addElement(con);
		logger.info("Connection created " + getLogInfo());
		return con;
	}

	public void returnConection(Connection con) {
		usedCons.remove(con);
		cons.add(con);
		logger.debug("Connection pooled " + getLogInfo());
	}

	private Connection createCon() {

		Connection con = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			con.setAutoCommit(true);
		} catch (ClassNotFoundException e) {
			System.out.println("cant load driver " + driver);
			logger.debug("Cann't load Driver " + driver);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;

	}

	private String getLogInfo() {
		return "[used:" + usedCons.size() + ",pooled:" + cons.size() + ",max:"
				+ max_cons + "]";
	}

	public int maxSize() {
		return max_cons;
	}

	public int usedSize() {
		return usedCons.size();
	}

	public int poolSize() {
		return cons.size();
	}

	public String getTable() {
		return table;
	}

}
