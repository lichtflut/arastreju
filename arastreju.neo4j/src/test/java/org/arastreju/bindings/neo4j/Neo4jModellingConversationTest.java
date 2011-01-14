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

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.arastreju.bindings.neo4j.impl.NeoDataStore;
import org.arastreju.sge.ModelingConversation;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.naming.SimpleNamespace;
import org.junit.Assert;
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
	
	@Test
	public void testInstantiation() throws IOException{
		ModelingConversation mc = new Neo4jModellingConversation(new NeoDataStore());
		
		ResourceNode node = new SNResource();
		node.setNamespace(new SimpleNamespace("http://q#"));
		node.setName("N1");
		mc.attach(node);
		
		mc.close();
	}
	
	@Test
	public void testFind() throws IOException{
		ModelingConversation mc = new Neo4jModellingConversation(new NeoDataStore());
		
		QualifiedName qn = new QualifiedName("http://q#", "N1");
		ResourceNode node = new SNResource(qn);
		mc.attach(node);
		
		ResourceNode node2 = mc.findResource(qn);
		
		assertNotNull(node2);
		
		mc.close();
	}
	
	@Test
	public void testMerge() throws IOException{
		ModelingConversation mc = new Neo4jModellingConversation(new NeoDataStore());
		
		QualifiedName qn = new QualifiedName("http://q#", "N1");
		ResourceNode node = new SNResource(qn);
		node = mc.attach(node);
		
		node = mc.attach(node);
		
		ResourceNode node2 = mc.findResource(qn);
		
		assertNotNull(node2);
		
		mc.close();
	}
	
	
	@Test
	public void testSNViews() throws IOException {
		final NeoDataStore store = new NeoDataStore();
		final Neo4jModellingConversation mc = new Neo4jModellingConversation(store);
		
		final QualifiedName qnVehicle = new QualifiedName("http://q#", "Verhicle");
		ResourceNode vehicle = new SNResource(qnVehicle);
		vehicle = mc.attach(vehicle);
		
		final QualifiedName qnCar = new QualifiedName("http://q#", "Car");
		ResourceNode car = new SNResource(qnCar);
		car = mc.attach(car);
		
		Association.create(car, RDFS.SUB_CLASS_OF, vehicle, null);
		
		mc.getRegistry().clear();
		
		car = mc.findResource(qnCar);
		vehicle = mc.findResource(qnVehicle);
		
		Assert.assertTrue(car.asClass().isSpecializationOf(vehicle));
		
		mc.close();
	}
	
}
