/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.io;

import junit.framework.Assert;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.DefaultSemanticGraph;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;

import java.io.IOException;
import java.util.Arrays;

/**
 * <p>
 *  Test case for rdf xml binding.
 * </p>
 *
 * <p>
 * 	Created Jan 12, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class RdfXmlBindingTest {
	
	
	@Test
	public void testXmlWriter() throws RDFHandlerException, IOException, SemanticIOException{
		final QualifiedName qnVehicle = new QualifiedName("http://q#", "Verhicle");
		final ResourceNode vehicle = new SNResource(qnVehicle);
		
		final QualifiedName qnCar = new QualifiedName("http://q#", "Car");
		final ResourceNode car = new SNResource(qnCar);
		
		final Statement association = SNOPS.associate(car, RDFS.SUB_CLASS_OF, vehicle);
		
		SemanticGraph graph = new DefaultSemanticGraph(Arrays.asList(association));
		Assert.assertNotNull(graph);
	}
	
	@Test
	public void testXmlReader() throws RDFHandlerException, IOException, SemanticIOException{
		final SemanticGraphIO io = new RdfXmlBinding();
		final SemanticGraph graph = io.read(getClass().getClassLoader().getResourceAsStream("test-input.rdf.xml"));
		Assert.assertNotNull(graph);
		
		System.out.println(graph);
		
	}
	
}
