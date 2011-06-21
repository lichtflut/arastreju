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

import static org.arastreju.bindings.neo4j.util.test.Dumper.dumpDeep;
import static org.arastreju.sge.SNOPS.associate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

import org.arastreju.bindings.neo4j.ArasRelTypes;
import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
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
		
		Relationship rel = a.createRelationshipTo(b, ArasRelTypes.VALUE);
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
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
		
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
		
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
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
		
		Association.create(car, RDFS.SUB_CLASS_OF, vehicle);
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
		
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
		
		final ResourceNode subClasss = car2.getSingleAssociationClient(RDFS.SUB_CLASS_OF).asResource();
		assertEquals(vehicle, subClasss);
		
		final ValueNode brandname = car2.getSingleAssociationClient(Aras.HAS_BRAND_NAME).asValue();
		assertEquals(brandname.getStringValue(), "BMW");

		final ValueNode propername = car2.getSingleAssociationClient(Aras.HAS_PROPER_NAME).asValue();
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
		
		final Association stored = car1.getSingleAssociation(Aras.HAS_BRAND_NAME);
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
		
		final Context[] cl1 = car2.getSingleAssociation(Aras.HAS_BRAND_NAME).getContexts();
		final Context[] cl2 = car2.getSingleAssociation(RDFS.SUB_CLASS_OF).getContexts();
		final Context[] cl3 = car2.getSingleAssociation(Aras.HAS_PROPER_NAME).getContexts();
		
		System.out.println(Arrays.toString(cl1));
		
		assertArrayEquals(new Context[] {ctx1}, cl1);
		assertArrayEquals(new Context[] {ctx1, ctx2}, cl2);
		assertArrayEquals(new Context[] {ctx1, ctx2, ctx3}, cl3);
		assertArrayEquals(new Context[] {ctx1, ctx2, ctx3}, cl3);
	}

}