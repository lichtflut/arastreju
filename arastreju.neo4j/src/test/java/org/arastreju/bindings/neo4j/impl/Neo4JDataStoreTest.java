/*
 * Copyright (C) Risk Management Solutions GmbH
 */
package org.arastreju.bindings.neo4j.impl;

import static org.arastreju.bindings.neo4j.util.test.Dumper.dumpDeep;

import java.io.IOException;

import org.arastreju.bindings.neo4j.NeoPredicate;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.Assert;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;

/**
 * [Description]
 *
 * @author Oliver Tigges
 */
public class Neo4JDataStoreTest {
	
	@Test
	public void testIndexing() throws IOException {
		final Neo4jDataStore store = new Neo4jDataStore();
		
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
		
		Assert.assertTrue(a2.getRelationships().iterator().hasNext());
		
		Relationship relationship = a2.getRelationships().iterator().next();
		
		Assert.assertEquals(a, relationship.getStartNode());
		Assert.assertEquals(b, relationship.getEndNode());
		Assert.assertEquals("123", relationship.getProperty("type"));
		
		tx.success();
		tx.finish();
		
	}
	
	@Test
	public void testDetaching() throws IOException{
		final Neo4jDataStore store = new Neo4jDataStore();
		
		final QualifiedName qnCar = new QualifiedName("http://q#", "Car");
		final ResourceNode car = new SNResource(qnCar);
		
		final ResourceNode car2 = store.attach(car);
		Assert.assertSame(car, car2);
		
		final ResourceNode car3 = store.findResource(qnCar);
		Assert.assertSame(car, car3);
		
		store.detach(car);

		final ResourceNode car4 = store.findResource(qnCar);
		Assert.assertNotSame(car, car4);
		
		store.close();
	}

}
