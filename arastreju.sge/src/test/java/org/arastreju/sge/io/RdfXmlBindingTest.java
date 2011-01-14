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
package org.arastreju.sge.io;

import java.io.IOException;
import java.util.Arrays;

import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.DefaultSemanticGraph;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;



/**
 * <p>
 *  [DESCRIPTION]
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
	public void testXmlWriter() throws RDFHandlerException, IOException, OntologyIOException{
		final SemanticGraphIO io = new RdfXmlBinding();

		final QualifiedName qnVehicle = new QualifiedName("http://q#", "Verhicle");
		final ResourceNode vehicle = new SNResource(qnVehicle);
		
		final QualifiedName qnCar = new QualifiedName("http://q#", "Car");
		final ResourceNode car = new SNResource(qnCar);
		
		final Association association = Association.create(car, RDFS.SUB_CLASS_OF, vehicle, null);
		
		SemanticGraph graph = new DefaultSemanticGraph(Arrays.asList(association));
		
		io.write(graph, System.out);

	}
	
	@Test
	public void testXmlReader() throws RDFHandlerException, IOException, OntologyIOException{
		final SemanticGraphIO io = new RdfXmlBinding();
		
		SemanticGraph graph = io.read(getClass().getClassLoader().getResourceAsStream("n4.aras.rdf"));
		
		System.out.println(graph);
	}
	
	@Test
	public void testReoundtrip() throws IOException, OntologyIOException {
		final SemanticGraphIO io = new RdfXmlBinding();
		
		final SemanticGraph graph = io.read(getClass().getClassLoader().getResourceAsStream("n4.aras.rdf"));
		io.write(graph, System.out);
		
	}
	
	
}
