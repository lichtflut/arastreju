package org.arastreju.rdb.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.arastreju.bindings.rdb.RdbGateFactory;
import org.arastreju.bindings.rdb.jdbc.DBOperations;
import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.context.DomainIdentifier;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdbTest {

	private final Logger logger = LoggerFactory.getLogger(RdbTest.class);
	
	final private ArastrejuProfile profile = ArastrejuProfile.read("arastreju.default.profile");;
	
	@After
	public void tearDown(){
		
		String driver = profile.getProperty("org.arastreju.bindings.rdb.jdbcDriver");
		String user =  profile.getProperty("org.arastreju.bindings.rdb.dbUser");
		String pass =  profile.getProperty("org.arastreju.bindings.rdb.dbPass");
		String url = profile.getProperty("org.arastreju.bindings.rdb.protocol")+
				profile.getProperty("org.arastreju.bindings.rdb.db");
		
		Connection con;
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);
			con.setAutoCommit(true);
			DBOperations.deleteTable(con, DomainIdentifier.MASTER_DOMAIN);
		} catch (ClassNotFoundException e) {
			System.out.println("cant load driver "+driver);
			logger.debug("Cann't load Driver "+driver);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLoadProfile() {
		
		assertNotNull(profile);
		assertTrue(profile.getProperty(ArastrejuProfile.GATE_FACTORY).equals(RdbGateFactory.class.getName()));
	}
	
	@Test
	public void testInit(){
		
		Arastreju aras = Arastreju.getInstance(profile);
		ArastrejuGate gate = aras.openMasterGate();
		
		ModelingConversation mc = gate.startConversation();
		
	}

}
