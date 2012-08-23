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

import static org.junit.Assert.*;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.arastreju.bindings.rdb.RdbGateFactory;
import org.arastreju.bindings.rdb.jdbc.ConnectionWraper;
import org.arastreju.bindings.rdb.jdbc.DBOperations;
import org.arastreju.sge.Arastreju;
import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.ArastrejuProfile;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.context.DomainIdentifier;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.FieldParam;
import org.arastreju.sge.query.QueryParam;
import org.arastreju.sge.query.ValueParam;
import org.bindings.arastreju.rdb.query.RdbQueryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

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
	
	@Before
	public void init(){
	
		Arastreju aras = Arastreju.getInstance(profile);
		ArastrejuGate gate = aras.openMasterGate();
		this.mc = gate.startConversation();
		
	}
	
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
			DBOperations.deleteTable(con, DomainIdentifier.MASTER_DOMAIN);
			mc.close();
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
	public void testResolveAndFind() throws IOException {

		ResourceNode found = mc.findResource(qnVehicle);
		assertNull(found);

		ResourceNode resolved = mc.resolve(SNOPS.id(qnVehicle));
		assertNotNull(resolved);

		resolved.addAssociation(SNOPS.id(qnKnows), mc.resolve(SNOPS.id(qnBike)), null);

		found = mc.findResource(qnVehicle);
		assertNotNull(found);
	}

	
	@Test
	public void testDetaching() throws IOException{


		final ResourceNode car = new SNResource(qnCar);

		car.addAssociation(SNOPS.id(qnKnows), mc.resolve(SNOPS.id(qnVehicle)), null);
		mc.attach(car);

		final ResourceNode car3 = mc.findResource(qnCar);
		assertEquals(car, car3);

		mc.detach(car);

		final ResourceNode car4 = mc.findResource(qnCar);
		assertNotSame(car, car4);
	}
	
	@Test
	public void testDatatypes() throws IOException {
		final ResourceNode car = new SNResource(qnCar);

		mc.attach(car);

		SNOPS.associate(car, Aras.HAS_PROPER_NAME, new SNText("Audi"));
		mc.detach(car);

		final ResourceNode car2 = mc.findResource(qnCar);
		assertNotSame(car, car2);
		final ValueNode value = SNOPS.singleObject(car2, Aras.HAS_PROPER_NAME).asValue();
		assertEquals(value.getStringValue(), "Audi");
	}
	
	@Test
	public void testPersisting() throws IOException {
		
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car = new SNResource(qnCar);
		
		SNOPS.associate(car, RDFS.SUB_CLASS_OF, vehicle);
		SNOPS.associate(car, Aras.HAS_PROPER_NAME, new SNText("Audi"));

		mc.attach(car);


		// detach and find again
		mc.detach(car);
		final ResourceNode car2 = mc.findResource(qnCar);
		assertNotSame(car, car2);


		final ResourceNode res = SNOPS.singleObject(car2, RDFS.SUB_CLASS_OF).asResource();

		final ValueNode value = SNOPS.singleObject(car2, Aras.HAS_PROPER_NAME).asValue();
		assertEquals(value.getStringValue(), "Audi");
	}
	
	@Test
	public void testMerging() throws IOException {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car1 = new SNResource(qnCar);
		
		mc.attach(car1);
		
		SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		
		// detach 
		mc.detach(car1);
		mc.detach(vehicle);
		
		SNOPS.associate(car1, RDFS.SUB_CLASS_OF, vehicle);
		SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));

		// attach again
		mc.attach(car1);
		
		// detach and find again
		mc.detach(car1);
		final ResourceNode car2 = mc.findResource(qnCar);
		assertNotNull(car2);
		assertNotSame(car1, car2);
		
		final ResourceNode subClasss = SNOPS.singleObject(car2, RDFS.SUB_CLASS_OF).asResource();
		assertEquals(vehicle, subClasss);
		
		final ValueNode brandname = SNOPS.singleObject(car2, Aras.HAS_BRAND_NAME).asValue();
		assertEquals(brandname.getStringValue(), "BMW");

		final ValueNode propername = SNOPS.singleObject(car2, Aras.HAS_PROPER_NAME).asValue();
		assertEquals(propername.getStringValue(), "Knut");
	}
	
	@Test
	public void testDirectRemoval() {
		
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car1 = new SNResource(qnCar);
		
		final Statement association = SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		SNOPS.associate(car1, RDFS.SUB_CLASS_OF, vehicle);
		SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));
		
		mc.attach(car1);
		
		final Statement stored = SNOPS.singleAssociation(car1, Aras.HAS_BRAND_NAME);
		assertEquals(association.hashCode(), stored.hashCode());
		
		assertEquals(3, car1.getAssociations().size());
		assertFalse(car1.getAssociations(Aras.HAS_BRAND_NAME).isEmpty());
		assertTrue("Association not present", car1.getAssociations().contains(association));
		
		final boolean removedFlag = car1.removeAssociation(association);
		assertTrue(removedFlag);
		
		assertEquals(2, car1.getAssociations().size());
		assertTrue(car1.getAssociations( Aras.HAS_BRAND_NAME).isEmpty());
		
	}
	
	@Test
	public void testAttachingRemoval() {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car1 = new SNResource(qnCar);
		
		mc.attach(car1);
		
		final Statement association = SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		SNOPS.associate(car1, RDFS.SUB_CLASS_OF, vehicle);
		SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));
		
		// detach 
		mc.detach(car1);
		
		assertEquals(3, car1.getAssociations().size());
		assertFalse(car1.getAssociations(Aras.HAS_BRAND_NAME).isEmpty());
		assertTrue("Association not present", car1.getAssociations().contains(association));
		
		final boolean removedFlag = car1.removeAssociation(association);
		assertTrue(removedFlag);
		
		mc.attach(car1);
		
		final ResourceNode car2 = mc.findResource(qnCar);
		assertNotSame(car1, car2);
		
		assertEquals(2, car2.getAssociations().size());
		assertTrue(car2.getAssociations( Aras.HAS_BRAND_NAME).isEmpty());
		
	}
	
	@Test
	public void testRemove() {
		final SNClass vehicle = new SNResource(qnVehicle).asClass();
		final SNClass car = new SNResource(qnCar).asClass();
		final SNClass bike = new SNResource(qnBike).asClass();
		
		final ResourceNode car1 = car.createInstance();
		
		SNOPS.associate(vehicle, RDFS.SUB_CLASS_OF, RDF.TYPE);
		SNOPS.associate(car, RDFS.SUB_CLASS_OF, vehicle);
		SNOPS.associate(bike, RDFS.SUB_CLASS_OF, vehicle);
		
		mc.attach(vehicle);
		mc.attach(bike);
		
		SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));
		
		mc.attach(car1);

		mc.remove(car);
		assertNull(mc.findResource(qnCar));
		mc.remove(car1);
		
		assertTrue(car1.getAssociations().isEmpty());
		assertFalse(car1.isAttached());
		assertTrue(car.getAssociations().isEmpty());
		assertFalse(car.isAttached());
		
		assertFalse(vehicle.getAssociations().isEmpty());
		assertTrue(vehicle.isAttached());
		
		mc.detach(vehicle);
		
		ResourceNode found = mc.findResource(qnVehicle);
		assertNotNull(found);
		assertEquals(RDF.TYPE, SNOPS.singleObject(found, RDFS.SUB_CLASS_OF));
	}
	
	@Test
	public void testQuerry(){
		String driver = profile.getProperty("org.arastreju.bindings.rdb.jdbcDriver");
		String user =  profile.getProperty("org.arastreju.bindings.rdb.dbUser");
		String pass =  profile.getProperty("org.arastreju.bindings.rdb.dbPass");
		String url = profile.getProperty("org.arastreju.bindings.rdb.protocol")+
				profile.getProperty("org.arastreju.bindings.rdb.db");
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(driver);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		cpds.setUser(user);
		cpds.setDataSourceName(url);
		cpds.setPassword(pass);
		ConnectionWraper cw = new ConnectionWraper(cpds, "test");
		RdbQueryBuilder query = new RdbQueryBuilder(cw);
		query
				.addValue("Automobil")
				.and().not()
				.add(new FieldParam(qnBike, "aBike"));
		query.getResult();
	}
	
}
