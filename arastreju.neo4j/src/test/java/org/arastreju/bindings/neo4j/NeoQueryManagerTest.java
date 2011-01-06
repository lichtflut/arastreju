/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;


import java.util.List;

import junit.framework.Assert;

import org.arastreju.bindings.neo4j.impl.NeoDataStore;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
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
		Association.create(car, Aras.HAS_PROPER_NAME, new SNText("BMW"), null);
		store.attach(car);
		
		final List<ResourceNode> result = qm.findByTag("BMW");
		
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.contains(car));
	}

}
