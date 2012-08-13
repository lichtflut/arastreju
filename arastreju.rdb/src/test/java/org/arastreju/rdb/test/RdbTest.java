/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package org.arastreju.rdb.test;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created 25.07.2012
 * </p>
 *
 * @author Raphael Esterle

 */

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.arastreju.bindings.rdb.RdbGateFactory;
import org.arastreju.bindings.rdb.jdbc.Column;
import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OT: Marked to be ignored - do not work if no MySQL is running.
 */
//@Ignore
public class RdbTest {

	private final Logger logger = LoggerFactory.getLogger(RdbTest.class);
	
	final private ArastrejuProfile profile = ArastrejuProfile.read("arastreju.default.profile");
	
	private static ModelingConversation mc;
	
	private final QualifiedName qnVehicle = new QualifiedName("http://q#",
			"Verhicle");
	private final QualifiedName qnCar = new QualifiedName("http://q#", "Car");
	private final QualifiedName qnBike = new QualifiedName("http://q#", "Bike");

	private final QualifiedName qnEmployedBy = new QualifiedName("http://q#",
			"employedBy");
	private final QualifiedName qnHasEmployees = new QualifiedName("http://q#",
			"hasEmployees");
	private final QualifiedName qnKnows = new QualifiedName("http://q#",
			"knows");
	private BigInteger one = new BigInteger("1");
	
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
			//DBOperations.deleteTable(con, DomainIdentifier.MASTER_DOMAIN);
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
		
		this.mc = gate.startConversation();
		
		Map<String, String> c = new HashMap<String, String>();
		c.put(Column.SUBJECT.value(), "SUBJECT");
		c.put(Column.PREDICATE.value(), "PREDICATE");
		c.put(Column.OBJECT.value(), "OBJECT");
		
		ResourceNode n0 = new SNResource(qnCar);
		ResourceNode n1 = new SNResource(qnBike);
		ResourceNode n2 = new SNResource(qnVehicle);
		SNOPS.associate(n1, SNOPS.id(qnKnows), n0, null);
		SNOPS.associate(n1, SNOPS.id(qnHasEmployees), new SNValue(ElementaryDataType.INTEGER, one), null);
		SNOPS.associate(n1, SNOPS.id(qnKnows), n2, null);
		mc.attach(n1);
	}
	
	@Test
	public void testResolve(){
		// Resolve existing Node
		ResourceNode node = mc.resolve(SNOPS.id(qnBike));
		assertNotNull(node);
		assertTrue(node.getAssociations().size()==3);
		
		// Resolve a non existant Node
		node = mc.resolve(SNOPS.id(qnEmployedBy));
		assertNotNull(node);
		assertTrue(node.getAssociations().size()==0);
		
	}
	
	@Test
	public void tesFind(){
		mc.findResource(qnKnows);
	}

}
