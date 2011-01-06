/*
 * Copyright (C) Risk Management Solutions GmbH
 */
package org.arastreju.bindings.neo4j.impl;

import static org.arastreju.bindings.neo4j.util.test.Dumper.dumpDeep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.NeoPredicate;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;

/**
 * <p>
 *  Test cases for {@link NeoDataStore}.
 * </p>
 *
 * <p>
 * 	Created Dec 2, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoDataStoreTest {
	
	private final QualifiedName qnVehicle = new QualifiedName("http://q#", "Verhicle");
	private final QualifiedName qnCar = new QualifiedName("http://q#", "Car");
	
	private NeoDataStore store;
	
	// -----------------------------------------------------
	
	@Before
	public void setUp() throws IOException{
		store = new NeoDataStore();	
	}
	
	@After
	public void tearDown(){
		store.close();
	}
	
	// -----------------------------------------------------

	@Test
	public void testResourceIndexing() throws IOException {
		final IndexService index = store.getIndexService();
		
		final GraphDatabaseService gdbService = store.getGdbService();
		Transaction tx = gdbService.beginTx();
		
		final Node a = gdbService.createNode();
		
		index.index(a, "name", "a");
		final Node b = gdbService.createNode();
		
		tx.success();
		tx.finish();
		
		tx = gdbService.beginTx();
		
		Relationship rel = a.createRelationshipTo(b, new NeoPredicate(new QualifiedName("http://q#p")));
		rel.setProperty("type", "123");
		
		tx.success();
		tx.finish();
		
		tx = gdbService.beginTx();
		
		System.out.println(dumpDeep(a));
		
		final Node a2 = index.getSingleNode("name", "a");
		
		System.out.println(dumpDeep(a2));
		
		assertTrue(a2.getRelationships().iterator().hasNext());
		
		Relationship relationship = a2.getRelationships().iterator().next();
		
		assertEquals(a, relationship.getStartNode());
		assertEquals(b, relationship.getEndNode());
		assertEquals("123", relationship.getProperty("type"));
		
		tx.success();
		tx.finish();
	}
	
	@Test
	public void testValueIndexing() throws IOException {
		final IndexService index = store.getIndexService();
		
		final ResourceNode car = new SNResource(qnCar);
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"), null);
		
		store.attach(car);
		
		final GraphDatabaseService gdbService = store.getGdbService();
		Transaction tx = gdbService.beginTx();
		
		Node found = index.getSingleNode(NeoConstants.INDEX_KEY_RESOURCE_VALUE, "BMW");
		assertNotNull(found);
		
		tx.finish();
	}
	
	@Test
	public void testDetaching() throws IOException{
		final ResourceNode car = new SNResource(qnCar);
		
		final ResourceNode car2 = store.attach(car);
		assertSame(car, car2);
		
		final ResourceNode car3 = store.findResource(qnCar);
		assertSame(car, car3);
		
		store.detach(car);

		final ResourceNode car4 = store.findResource(qnCar);
		assertNotSame(car, car4);
	}
	
	@Test
	public void testDatatypes() throws IOException {
		final ResourceNode car = new SNResource(qnCar);
		
		store.attach(car);
		
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"), null);
		store.detach(car);
		
		final ResourceNode car2 = store.findResource(qnCar);
		assertNotSame(car, car2);
		final ValueNode value = car2.getSingleAssociationClient(Aras.HAS_PROPER_NAME).asValue();
		
		assertEquals(value.getStringValue(), "BMW");
	}
	
	@Test
	public void testPersisting() throws IOException {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car = new SNResource(qnCar);
		
		Association.create(car, RDFS.SUB_CLASS_OF, vehicle, null);
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"), null);
		
		store.attach(car);
		
		// detach and find again
		store.detach(car);
		final ResourceNode car2 = store.findResource(qnCar);
		assertNotSame(car, car2);

		final ResourceNode res = car2.getSingleAssociationClient(RDFS.SUB_CLASS_OF).asResource();
		assertEquals(vehicle, res);
		
		final ValueNode value = car2.getSingleAssociationClient(Aras.HAS_PROPER_NAME).asValue();
		assertEquals(value.getStringValue(), "BMW");
	}
	
	@Test
	public void testMerging() throws IOException {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car1 = new SNResource(qnCar);
		
		store.attach(car1);
		
		Association.create(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"), null);
		
		// detach 
		store.detach(car1);
		store.detach(vehicle);
		
		Association.create(car1, RDFS.SUB_CLASS_OF, vehicle, null);
		Association.create(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"), null);

		// attach again
		store.attach(car1);
		
		// detach and find again
		store.detach(car1);
		final ResourceNode car2 = store.findResource(qnCar);
		assertNotSame(car1, car2);
		
		final ResourceNode subClasss = car2.getSingleAssociationClient(RDFS.SUB_CLASS_OF).asResource();
		assertEquals(vehicle, subClasss);
		
		final ValueNode brandname = car2.getSingleAssociationClient(Aras.HAS_BRAND_NAME).asValue();
		assertEquals(brandname.getStringValue(), "BMW");

		final ValueNode propername = car2.getSingleAssociationClient(Aras.HAS_PROPER_NAME).asValue();
		assertEquals(propername.getStringValue(), "Knut");
	}

}
