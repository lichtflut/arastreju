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


import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.arastreju.bindings.neo4j.impl.NeoDataStore;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.views.SNEntity;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 *  Test cases for {@link NeoQueryManager}.
 * </p>
 *
 * <p>
 * 	Created Jan 6, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoQueryManagerTest {
	
	private final QualifiedName qnCar = new QualifiedName("http://q#", "Car");
	private final QualifiedName qnBike = new QualifiedName("http://q#", "Bike");
	
	private NeoDataStore store;
	private NeoQueryManager qm;
	
	// -----------------------------------------------------

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		store = new NeoDataStore();
		qm = new NeoQueryManager(store);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		store.close();
	}
	
	// -----------------------------------------------------
	
	@Test
	public void testFindByTag(){
		final ResourceNode car = new SNResource(qnCar);
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
		Association.create(car, RDFS.LABEL, new SNText("Automobil"));
		store.attach(car);
		
		final List<ResourceNode> result = qm.findByTag("BMW");
		
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.contains(car));
		
	}
	
	@Test
	public void testFindByPredicateAndTag(){
		final ResourceNode car = new SNResource(qnCar);
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"));
		Association.create(car, RDFS.LABEL, new SNText("Automobil"));
		store.attach(car);
		
		final List<ResourceNode> result = qm.findByTag(RDFS.LABEL, "Automobil");
		
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.contains(car));
	}
	
	@Test
	public void testFindByType(){
		final Context ctx = null;
		final ResourceNode car = new SNResource(qnCar);
		Association.create(car, RDF.TYPE, RDFS.CLASS, ctx);
		store.attach(car);
		
		final ResourceNode bike = new SNResource(qnBike);
		Association.create(bike, RDF.TYPE, RDFS.CLASS, ctx);
		store.attach(bike);
		
		final SNEntity aCar = car.asClass().createInstance(ctx);
		store.attach(aCar);
		
		final SNEntity aBike = bike.asClass().createInstance(ctx);
		store.attach(aBike);
		
		final List<ResourceNode> result = qm.findByType(new SimpleResourceID(qnCar));
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.contains(aCar));
		
		final List<ResourceNode> result2 = qm.findByType(new SimpleResourceID(qnBike));
		Assert.assertEquals(1, result2.size());
		Assert.assertTrue("Expected aBike to be a Bike", result2.contains(aBike));
		
		final List<ResourceNode> result3 = qm.findByType(RDFS.CLASS);
		Assert.assertEquals(2, result3.size());
		Assert.assertTrue("Expected Bike to be a Class", result3.contains(bike));
		Assert.assertTrue("Expected Car to be a Class", result3.contains(car));
	}
	
	@Test
	public void testFindIncomingAssociations() {
		final Context ctx = null;
		final ResourceNode car = new SNResource(qnCar);
		Association.create(car, RDF.TYPE, RDFS.CLASS);
		store.attach(car);
		
		final ResourceNode bike = new SNResource(qnBike);
		Association.create(bike, RDF.TYPE, RDFS.CLASS);
		store.attach(bike);

		final SNEntity car1 = car.asClass().createInstance(ctx);
		store.attach(car1);

		final SNEntity car2 = car.asClass().createInstance(ctx);
		store.attach(car2);
		
		store.detach(car1);
		store.detach(car2);
		store.detach(car);
		store.detach(bike);

		final Set<Statement> result = qm.findIncomingStatements(RDFS.CLASS);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertTrue(result.contains(new DetachedStatement(car, RDF.TYPE, RDFS.CLASS)));
		Assert.assertTrue(result.contains(new DetachedStatement(bike, RDF.TYPE, RDFS.CLASS)));
		
		final Set<Statement> result2 = qm.findIncomingStatements(bike);
		Assert.assertNotNull(result2);
		Assert.assertEquals(0, result2.size());
		
		final Set<Statement> result3 = qm.findIncomingStatements(car);
		Assert.assertNotNull(result3);
		Assert.assertEquals(2, result3.size());
		Assert.assertTrue(result3.contains(new DetachedStatement(car1, RDF.TYPE, car)));
		Assert.assertTrue(result3.contains(new DetachedStatement(car2, RDF.TYPE, car)));
		
		
	}
	

}
