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
package org.arastreju.bindings.neo4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.arastreju.bindings.neo4j.impl.GraphDataStore;
import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.io.RdfXmlBinding;
import org.arastreju.sge.io.SemanticGraphIO;
import org.arastreju.sge.io.SemanticIOException;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * <p>
 *  Test case for the {@link Neo4jModellingConversation}.
 * </p>
 *
 * <p>
 * 	Created Sep 9, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class Neo4jModellingConversationTest {
	
	private SemanticNetworkAccess sna;
	private GraphDataStore store;
	private Neo4jModellingConversation mc;
	
	// -----------------------------------------------------

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		store = new GraphDataStore();
		sna = new SemanticNetworkAccess(store);
		mc = new Neo4jModellingConversation(sna);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		mc.close();
		sna.close();
		store.close();
	}
	
	// ----------------------------------------------------
	
	@Test
	public void testInstantiation() throws IOException{
		ResourceNode node = new SNResource(new QualifiedName("http://q#", "N1"));
		mc.attach(node);
	}
	
	@Test
	public void testFind() throws IOException{
		QualifiedName qn = new QualifiedName("http://q#", "N1");
		ResourceNode node = new SNResource(qn);
		mc.attach(node);
		
		ResourceNode node2 = mc.findResource(qn);
		
		assertNotNull(node2);
	}
	
	@Test
	public void testMerge() throws IOException{
		QualifiedName qn = new QualifiedName("http://q#", "N1");
		ResourceNode node = new SNResource(qn);
		mc.attach(node);
		
		mc.attach(node);
		
		ResourceNode node2 = mc.findResource(qn);
		
		assertNotNull(node2);
	}
	
	
	@Test
	public void testSNViews() throws IOException {
		final QualifiedName qnVehicle = new QualifiedName("http://q#", "Verhicle");
		ResourceNode vehicle = new SNResource(qnVehicle);
		mc.attach(vehicle);
		
		final QualifiedName qnCar = new QualifiedName("http://q#", "Car");
		ResourceNode car = new SNResource(qnCar);
		mc.attach(car);
		
		SNOPS.associate(car, RDFS.SUB_CLASS_OF, vehicle);
		
		mc.getIndex().clearCache();
		
		car = mc.findResource(qnCar);
		vehicle = mc.findResource(qnVehicle);
		
		Assert.assertTrue(car.asClass().isSpecializationOf(vehicle));
	}
	
	@Test
	public void testGraphImport() throws IOException, SemanticIOException{
		final SemanticGraphIO io = new RdfXmlBinding();
		final SemanticGraph graph = io.read(getClass().getClassLoader().getResourceAsStream("test-statements.rdf.xml"));
		
		mc.attach(graph);
		
		final QualifiedName qn = new QualifiedName("http://test.arastreju.org/common#Person");
		final ResourceNode node = mc.findResource(qn);
		assertNotNull(node);
		
		final ResourceNode hasChild = mc.findResource(SNOPS.qualify("http://test.arastreju.org/common#hasChild"));
		assertNotNull(hasChild);
		assertEquals(new SimpleResourceID("http://test.arastreju.org/common#hasParent"), SNOPS.objects(hasChild, Aras.INVERSE_OF).iterator().next());
		
		final ResourceNode marriedTo = mc.findResource(SNOPS.qualify("http://test.arastreju.org/common#isMarriedTo"));
		assertNotNull(marriedTo);
		assertEquals(marriedTo, SNOPS.objects(marriedTo, Aras.INVERSE_OF).iterator().next());
	}
	
	@Test
	public void testSerialization() throws IOException, SemanticIOException, ClassNotFoundException {
		final SemanticGraphIO io = new RdfXmlBinding();
		final SemanticGraph graph = io.read(getClass().getClassLoader().getResourceAsStream("test-statements.rdf.xml"));
		mc.attach(graph);
		
		final QualifiedName qn = new QualifiedName("http://test.arastreju.org/common#Person");
		final ResourceNode node = mc.findResource(qn);
		
		assertTrue(node.isAttached());
		
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		new ObjectOutputStream(out).writeObject(node);
		
		byte[] bytes = out.toByteArray();
		out.close();
		
		final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
		
		final ResourceNode read = (ResourceNode) in.readObject();
		assertFalse(read.isAttached());
	}
	
}
