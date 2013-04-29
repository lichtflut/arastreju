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

import java.io.IOException;

import junit.framework.Assert;

import org.arastreju.sge.model.SemanticGraph;
import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;

/**
 * <p>
 *  Test cases for {@link JsonBinding}.
 * </p>
 *
 * <p>
 * 	Created Jan 12, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class JsonBindingTest {
	
	@Test
	public void testJsonWriter() throws RDFHandlerException, IOException, SemanticIOException{
		final SemanticGraph graph = new RdfXmlBinding().read(getClass().getClassLoader().getResourceAsStream("n04.aras.rdf"));
		Assert.assertNotNull(graph);
	}
	
	
}
