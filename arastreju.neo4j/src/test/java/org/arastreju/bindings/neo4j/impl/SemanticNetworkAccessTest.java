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
import static org.arastreju.sge.SNOPS.qualify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.io.OntologyIOException;
import org.arastreju.sge.io.RdfXmlBinding;
import org.arastreju.sge.io.SemanticGraphIO;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.QualifiedName;
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
	
	private SemanticNetworkAccess store;
	
	// -----------------------------------------------------
	
	@Before
	public void setUp() throws IOException{
		store = new SemanticNetworkAccess();	
	}
	
	@After
	public void tearDown(){
		store.close();
	}
	
	// -----------------------------------------------------

	@Test
	public void testResolveAndFind() throws IOException {
		ResourceNode found = store.findResource(qnVehicle);
		assertNull(found);
		
		ResourceNode resolved = store.resolve(SNOPS.id(qnVehicle));
		assertNotNull(resolved);
		
		found = store.findResource(qnVehicle);
		assertNotNull(found);
		
	}
	
	@Test
	public void testValueIndexing() throws IOException {
		final ResourceIndex index = store.getIndex();
		
		final ResourceNode car = new SNResource(qnCar);
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
		
		store.attach(car);
		
		final GraphDatabaseService gdbService = store.getGdbService();
		Transaction tx = gdbService.beginTx();
		
		List<ResourceNode> found = index.lookup(Aras.HAS_PROPER_NAME, "BMW");
		assertNotNull(found);
		assertEquals(1, found.size());
		
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
		
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
		store.detach(car);
		
		final ResourceNode car2 = store.findResource(qnCar);
		assertNotSame(car, car2);
		final ValueNode value = SNOPS.singleObject(car2, Aras.HAS_PROPER_NAME).asValue();
		
		assertEquals(value.getStringValue(), "BMW");
	}
	
	@Test
	public void testPersisting() throws IOException {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car = new SNResource(qnCar);
		
		Association.create(car, RDFS.SUB_CLASS_OF, vehicle);
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
		
		store.attach(car);
		
		// detach and find again
		store.detach(car);
		final ResourceNode car2 = store.findResource(qnCar);
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
		
		store.attach(car1);
		
		Association.create(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		
		// detach 
		store.detach(car1);
		store.detach(vehicle);
		
		Association.create(car1, RDFS.SUB_CLASS_OF, vehicle);
		Association.create(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));

		// attach again
		store.attach(car1);
		
		// detach and find again
		store.detach(car1);
		final ResourceNode car2 = store.findResource(qnCar);
		assertNotSame(car1, car2);
		
		final ResourceNode subClasss = SNOPS.singleObject(car2, RDFS.SUB_CLASS_OF).asResource();
		assertEquals(vehicle, subClasss);
		
		final ValueNode brandname = SNOPS.singleObject(car2, Aras.HAS_BRAND_NAME).asValue();
		assertEquals(brandname.getStringValue(), "BMW");

		final ValueNode propername = SNOPS.singleObject(car2, Aras.HAS_PROPER_NAME).asValue();
		assertEquals(propername.getStringValue(), "Knut");
	}

	@Test
	public void testGraphImport() throws IOException, OntologyIOException{
		final SemanticGraphIO io = new RdfXmlBinding();
		final SemanticGraph graph = io.read(getClass().getClassLoader().getResourceAsStream("n04.aras.rdf"));
		
		store.attach(graph);
		
		final QualifiedName qn = new QualifiedName("http://arastreju.org/kernel#BrandName");
		
		final ResourceNode node = store.findResource(qn);
		assertNotNull(node);
	}
	
	@Test
	public void testSerialization() throws IOException, OntologyIOException, ClassNotFoundException {
		final SemanticGraphIO io = new RdfXmlBinding();
		final SemanticGraph graph = io.read(getClass().getClassLoader().getResourceAsStream("n04.aras.rdf"));
		store.attach(graph);
		final QualifiedName qn = new QualifiedName("http://arastreju.org/kernel#BrandName");
		final ResourceNode node = store.findResource(qn);
		
		assertTrue(node.isAttached());
		
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		new ObjectOutputStream(out).writeObject(node);
		
		byte[] bytes = out.toByteArray();
		out.close();
		
		final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
		
		final ResourceNode read = (ResourceNode) in.readObject();
		assertFalse(read.isAttached());
	}
	
	@Test
	public void testBidirectionalAssociations() {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car = new SNResource(qnCar);
		
		final ResourceID pred1 = new SimpleResourceID("http://arastreju.org/stuff#", "P1");
		final ResourceID pred2 = new SimpleResourceID("http://arastreju.org/stuff#", "P2");
		
		SNOPS.associate(vehicle, pred1, car);
		SNOPS.associate(car, pred2, vehicle);
		store.attach(vehicle);
		
		final ResourceNode vehicleLoaded = store.findResource(qnVehicle);
		final ResourceNode carLoaded = store.findResource(qnCar);
		
		assertFalse(vehicleLoaded.getAssociations().isEmpty());
		assertFalse(carLoaded.getAssociations().isEmpty());
		
		assertFalse(vehicleLoaded.getAssociations(pred1).isEmpty());
		assertFalse(carLoaded.getAssociations(pred2).isEmpty());
	}
	
	@Test
	public void testDirectRemoval() {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car1 = new SNResource(qnCar);
		
		final Association association = Association.create(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		Association.create(car1, RDFS.SUB_CLASS_OF, vehicle);
		Association.create(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));
		
		store.attach(car1);
		
		final Association stored = SNOPS.singleAssociation(car1, Aras.HAS_BRAND_NAME);
		assertEquals(association.hashCode(), stored.hashCode());
		
		assertEquals(3, car1.getAssociations().size());
		assertFalse(car1.getAssociations(Aras.HAS_BRAND_NAME).isEmpty());
		assertTrue("Association not present", car1.getAssociations().contains(association));
		
		final boolean removedFlag = car1.remove(association);
		assertTrue(removedFlag);
		
		assertEquals(2, car1.getAssociations().size());
		assertTrue(car1.getAssociations( Aras.HAS_BRAND_NAME).isEmpty());
		
	}
	
	@Test
	public void testAttachingRemoval() {
		final ResourceNode vehicle = new SNResource(qnVehicle);
		final ResourceNode car1 = new SNResource(qnCar);
		
		store.attach(car1);
		
		final Association association = Association.create(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		Association.create(car1, RDFS.SUB_CLASS_OF, vehicle);
		Association.create(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));
		
		// detach 
		store.detach(car1);
		
		assertEquals(3, car1.getAssociations().size());
		assertFalse(car1.getAssociations(Aras.HAS_BRAND_NAME).isEmpty());
		assertTrue("Association not present", car1.getAssociations().contains(association));
		
		final boolean removedFlag = car1.remove(association);
		assertTrue(removedFlag);
		
		store.attach(car1);
		
		final ResourceNode car2 = store.findResource(qnCar);
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
		
		store.attach(car1);
		
		associate(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"), ctx1);
		associate(car1, RDFS.SUB_CLASS_OF, vehicle, ctx1, ctx2);
		associate(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"), ctx1, ctx2, ctx3);
		
		// detach 
		store.detach(car1);
		
		final ResourceNode car2 = store.findResource(qnCar);
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
		
		Association.create(vehicle, RDFS.SUB_CLASS_OF, RDF.TYPE);
		Association.create(car, RDFS.SUB_CLASS_OF, vehicle);
		Association.create(bike, RDFS.SUB_CLASS_OF, vehicle);
		
		store.attach(vehicle);
		store.attach(bike);
		
		Association.create(car1, Aras.HAS_BRAND_NAME, new SNText("BMW"));
		Association.create(car1, Aras.HAS_PROPER_NAME, new SNText("Knut"));
		
		store.attach(car1);

		store.remove(car, true);
		assertNull(store.findResource(qnCar));
		store.remove(car1, true);
		
		assertTrue(car1.getAssociations().isEmpty());
		assertFalse(car1.isAttached());
		assertTrue(car.getAssociations().isEmpty());
		assertFalse(car.isAttached());
		
		assertFalse(vehicle.getAssociations().isEmpty());
		assertTrue(vehicle.isAttached());
		
		store.detach(vehicle);
		
		ResourceNode found = store.findResource(qnVehicle);
		assertNotNull(found);
		assertEquals(RDF.TYPE, SNOPS.singleObject(found, RDFS.SUB_CLASS_OF));
		
		assertNotNull(store.findResource(RDF.TYPE.getQualifiedName()));
		store.remove(bike, true);
		assertNull(store.findResource(RDF.TYPE.getQualifiedName()));
		
	}
	
	@Test
	public void testInferencing() {
		final ResourceNode hasEmployees = new SNResource(qnHasEmployees);
		final ResourceNode isEmployedBy = new SNResource(qnEmployedBy);
		
		associate(hasEmployees, Aras.INVERSE_OF, isEmployedBy);
		associate(isEmployedBy, Aras.INVERSE_OF, hasEmployees);
		
		store.attach(hasEmployees);
		
		// preparation done.
		
		ResourceNode mike = new SNResource(qualify("http://q#Mike"));
		ResourceNode corp = new SNResource(qualify("http://q#Corp"));
		
		store.attach(mike);
		
		associate(mike, isEmployedBy, corp);
		
		store.detach(corp);
		
		corp = store.findResource(corp.getQualifiedName());
		
		System.out.println(corp.getAssociations());
		
	}

}
