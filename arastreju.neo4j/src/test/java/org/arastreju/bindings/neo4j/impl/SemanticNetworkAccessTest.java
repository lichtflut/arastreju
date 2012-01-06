/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.bindings.neo4j.impl;

import static org.arastreju.sge.SNOPS.associate;
import static org.arastreju.sge.SNOPS.id;
import static org.arastreju.sge.SNOPS.qualify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.QueryResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

/**
 * <p>
 *  Test cases for {@link SemanticNetworkAccess}.
 * </p>
 *
 * <p>
 * 	Created Dec 2, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class SemanticNetworkAccessTest {
	
	private final QualifiedName qnVehicle = new QualifiedName("http://q#", "Verhicle");
	private final QualifiedName qnCar = new QualifiedName("http://q#", "Car");
	private final QualifiedName qnBike = new QualifiedName("http://q#", "Bike");
	
	private final QualifiedName qnEmployedBy = new QualifiedName("http://q#", "employedBy");
	private final QualifiedName qnHasEmployees = new QualifiedName("http://q#", "hasEmployees");
	private final QualifiedName qnKnows = new QualifiedName("http://q#", "knows");
	
	private SemanticNetworkAccess sna;
	private GraphDataStore store;
	
	// -----------------------------------------------------

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		store = new GraphDataStore();
		sna = new SemanticNetworkAccess(store);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		sna.close();
		store.close();
	}
	
	// -----------------------------------------------------

	@Test
	public void testResolveAndFind() throws IOException {
		ResourceNode found = sna.findResource(qnVehicle);
		assertNull(found);
		
		ResourceNode resolved = sna.resolve(SNOPS.id(qnVehicle));
		assertNotNull(resolved);
		
		found = sna.findResource(qnVehicle);
		assertNotNull(found);
		
	}
	
	@Test
	public void testValueIndexing() throws IOException {
		final ResourceIndex index = sna.getIndex();
		
		final ResourceNode car = new SNResource(qnCar);
		SNOPS.associate(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
		
		sna.attach(car);
		
		final GraphDatabaseService gdbService = sna.getGdbService();
		Transaction tx = gdbService.beginTx();
		
		final QueryResult found = index.lookup(Aras.HAS_PROPER_NAME, "BMW");
		assertNotNull(found);
		assertEquals(1, found.size());
		
		tx.finish();
	}
	
	@Test
	public void testDetaching() throws IOException{
		final ResourceNode car = new SNResource(qnCar);
		sna.attach(car);
		
		final ResourceNode car3 = sna.findResource(qnCar);
		assertEquals(car, car3);
		
		sna.detach(car);

		final ResourceNode car4 = sna.findResource(qnCar);
		assertNotSame(car, car4);
	}
	
	@Test
	public void testDatatypes() throws IOException {
		final ResourceNode car = new SNResource(qnCar);
		
		sna.attach(car);
		
		SNOPS.associate(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
		sna.detach(car);
		
		final ResourceNode car2 = sna.findResource(qnCar);
		assertNotSame(car, car2);
		final ValueNode value = SNOPS.singleObject(car2, Aras.HAS_PROPER_NAME).asValue();
		
		assertEquals(value.getStringValue(), "BMW");
	}
	
	@Test
	public void testPersisting() throws IOException {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car = new SNResource(qnCar);
		
		SNOPS.associate(car, RDFS.SUB_CLASS_OF, vehicle);
		SNOPS.associate(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
		
		sna.attach(car);
		
		// detach and find again
		sna.detach(car);
		final ResourceNode car2 = sna.findResource(qnCar);
		assertNotSame(car, car2);

		final ResourceNode res = SNOPS.singleObject(car2, RDFS.SUB_CLASS_OF).asResource();
		assertEquals(vehicle, res);
		
		final ValueNode value = SNOPS.singleObject(car2, Aras.HAS_PROPER_NAME).asValue();
		assertEquals(value.getStringValue(), "BMW");
	}
	
	@Test
	public void testMerging() throws IOException {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car1 = new SNResource(qnCar);
		
		sna.attach(car1);
		
		SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		
		// detach 
		sna.detach(car1);
		sna.detach(vehicle);
		
		SNOPS.associate(car1, RDFS.SUB_CLASS_OF, vehicle);
		SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));

		// attach again
		sna.attach(car1);
		
		// detach and find again
		sna.detach(car1);
		final ResourceNode car2 = sna.findResource(qnCar);
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
	public void testBidirectionalAssociations() {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car = new SNResource(qnCar);
		
		final ResourceID pred1 = new SimpleResourceID("http://arastreju.org/stuff#", "P1");
		final ResourceID pred2 = new SimpleResourceID("http://arastreju.org/stuff#", "P2");
		
		SNOPS.associate(vehicle, pred1, car);
		SNOPS.associate(car, pred2, vehicle);
		sna.attach(vehicle);
		
		final ResourceNode vehicleLoaded = sna.findResource(qnVehicle);
		final ResourceNode carLoaded = sna.findResource(qnCar);
		
		assertFalse(vehicleLoaded.getAssociations().isEmpty());
		assertFalse(carLoaded.getAssociations().isEmpty());
		
		assertFalse(vehicleLoaded.getAssociations(pred1).isEmpty());
		assertFalse(carLoaded.getAssociations(pred2).isEmpty());
	}
	
	@Test
	public void testDirectRemoval() {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car1 = new SNResource(qnCar);
		
		final Statement association = SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		SNOPS.associate(car1, RDFS.SUB_CLASS_OF, vehicle);
		SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));
		
		sna.attach(car1);
		
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
		
		sna.attach(car1);
		
		final Statement association = SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		SNOPS.associate(car1, RDFS.SUB_CLASS_OF, vehicle);
		SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));
		
		// detach 
		sna.detach(car1);
		
		assertEquals(3, car1.getAssociations().size());
		assertFalse(car1.getAssociations(Aras.HAS_BRAND_NAME).isEmpty());
		assertTrue("Association not present", car1.getAssociations().contains(association));
		
		final boolean removedFlag = car1.removeAssociation(association);
		assertTrue(removedFlag);
		
		sna.attach(car1);
		
		final ResourceNode car2 = sna.findResource(qnCar);
		assertNotSame(car1, car2);
		
		assertEquals(2, car2.getAssociations().size());
		assertTrue(car2.getAssociations( Aras.HAS_BRAND_NAME).isEmpty());
		
	}
	
	@Test
	public void testMultipleContexts() {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car1 = new SNResource(qnCar);
		
		final String ctxNamepsace = "http://lf.de/ctx#";
		final SimpleContextID ctx1 = new SimpleContextID(ctxNamepsace, "ctx1");
		final SimpleContextID ctx2 = new SimpleContextID(ctxNamepsace, "ctx2");
		final SimpleContextID ctx3 = new SimpleContextID(ctxNamepsace, "ctx3");
		
		sna.attach(car1);
		
		associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"), ctx1);
		associate(car1, RDFS.SUB_CLASS_OF, vehicle, ctx1, ctx2);
		associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"), ctx1, ctx2, ctx3);
		
		// detach 
		sna.detach(car1);
		
		final ResourceNode car2 = sna.findResource(qnCar);
		assertNotSame(car1, car2);
		
		final Context[] cl1 = SNOPS.singleAssociation(car2, Aras.HAS_BRAND_NAME).getContexts();
		final Context[] cl2 = SNOPS.singleAssociation(car2, RDFS.SUB_CLASS_OF).getContexts();
		final Context[] cl3 = SNOPS.singleAssociation(car2, Aras.HAS_PROPER_NAME).getContexts();
		
		assertArrayEquals(new Context[] {ctx1}, cl1);
		assertArrayEquals(new Context[] {ctx1, ctx2}, cl2);
		assertArrayEquals(new Context[] {ctx1, ctx2, ctx3}, cl3);
		assertArrayEquals(new Context[] {ctx1, ctx2, ctx3}, cl3);
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
		
		sna.attach(vehicle);
		sna.attach(bike);
		
		SNOPS.associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		SNOPS.associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));
		
		sna.attach(car1);

		sna.remove(car);
		assertNull(sna.findResource(qnCar));
		sna.remove(car1);
		
		assertTrue(car1.getAssociations().isEmpty());
		assertFalse(car1.isAttached());
		assertTrue(car.getAssociations().isEmpty());
		assertFalse(car.isAttached());
		
		assertFalse(vehicle.getAssociations().isEmpty());
		assertTrue(vehicle.isAttached());
		
		sna.detach(vehicle);
		
		ResourceNode found = sna.findResource(qnVehicle);
		assertNotNull(found);
		assertEquals(RDF.TYPE, SNOPS.singleObject(found, RDFS.SUB_CLASS_OF));
	}
	
	@Test
	public void testInferencingInverseOfBidirectional() {
		final ResourceNode knows = new SNResource(qnKnows);
		
		associate(knows, Aras.INVERSE_OF, knows);
		
		sna.attach(knows);
		
		assertTrue(SNOPS.objects(knows, Aras.INVERSE_OF).contains(knows));
		
		sna.detach(knows);
		
		// preparation done.
		
		ResourceNode mike = new SNResource(qualify("http://q#Mike"));
		ResourceNode kent = new SNResource(qualify("http://q#Kent"));
		
		sna.attach(mike);
		
		associate(mike, knows, kent);
		
		sna.detach(kent);
		
		kent = sna.findResource(kent.getQualifiedName());
		
		assertTrue(SNOPS.objects(kent, knows).contains(mike));
		
	}
	
	@Test
	public void testInferencingInverseOf() {
		final ResourceNode hasEmployees = new SNResource(qnHasEmployees);
		final ResourceNode isEmployedBy = new SNResource(qnEmployedBy);
		
		associate(hasEmployees, Aras.INVERSE_OF, isEmployedBy);
		associate(isEmployedBy, Aras.INVERSE_OF, hasEmployees);
		
		sna.attach(hasEmployees);
		
		// preparation done.
		
		ResourceNode mike = new SNResource(qualify("http://q#Mike"));
		ResourceNode corp = new SNResource(qualify("http://q#Corp"));
		
		sna.attach(mike);
		
		associate(mike, isEmployedBy, corp);
		
		sna.detach(corp);
		
		corp = sna.findResource(corp.getQualifiedName());
		
		assertTrue(SNOPS.objects(corp, hasEmployees).contains(mike));
		assertTrue(SNOPS.objects(mike, isEmployedBy).contains(corp));
		
		SNOPS.remove(corp, hasEmployees);
		
		assertFalse(SNOPS.objects(corp, hasEmployees).contains(mike));
		
		mike = sna.findResource(mike.getQualifiedName());
		assertFalse(SNOPS.objects(mike, isEmployedBy).contains(corp));
		
	}
	
	@Test
	public void testInferencingSubClasses() {
		final SNClass vehicleClass = new SNResource(qnVehicle).asClass();
		final SNClass carClass = new SNResource(qnCar).asClass();
		SNOPS.associate(carClass, RDFS.SUB_CLASS_OF, vehicleClass);
		
		final SNEntity car = carClass.createInstance();
		final SNEntity vehicle = vehicleClass.createInstance();
		
		sna.attach(vehicle);
		sna.attach(car);
		
		QueryResult hits = sna.getIndex().lookup(RDF.TYPE, id(qnVehicle));
		assertEquals(2, hits.size());
		
		SNOPS.remove(car, RDF.TYPE);
		
		hits = sna.getIndex().lookup(RDF.TYPE, id(qnVehicle));
		assertEquals(1, hits.size());
	}

}
