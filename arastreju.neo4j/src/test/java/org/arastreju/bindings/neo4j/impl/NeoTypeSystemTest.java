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

import java.io.IOException;
import java.util.Set;

import junit.framework.Assert;

import org.arastreju.sge.io.OntologyIOException;
import org.arastreju.sge.io.RdfXmlBinding;
import org.arastreju.sge.io.SemanticGraphIO;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 *  Test cases for {@link NeoTypeSystem}.
 * </p>
 *
 * <p>
 * 	Created Jan 20, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoTypeSystemTest {
	
	private SemanticNetworkAccess store;
	private NeoTypeSystem typeSystem;
	
	// -----------------------------------------------------
	
	@Before
	public void setUp() throws IOException{
		store = new SemanticNetworkAccess();	
		typeSystem = new NeoTypeSystem(store);
	}
	
	@After
	public void tearDown(){
		store.close();
	}
	
	// -----------------------------------------------------
	
	
	@Test
	public void testFindClasses() throws IOException, OntologyIOException{
		final SemanticGraphIO io = new RdfXmlBinding();
		
		store.attach(io.read(getClass().getClassLoader().getResourceAsStream("n01.rdf.rdf")));
		store.attach(io.read(getClass().getClassLoader().getResourceAsStream("n02.rdfs.rdf")));
		store.attach(io.read(getClass().getClassLoader().getResourceAsStream("n03.owl.rdf")));
		store.attach(io.read(getClass().getClassLoader().getResourceAsStream("n04.aras.rdf")));
		
		Set<SNClass> classes = typeSystem.getAllClasses();
		
		Assert.assertNotNull(classes);
		
	}

}
