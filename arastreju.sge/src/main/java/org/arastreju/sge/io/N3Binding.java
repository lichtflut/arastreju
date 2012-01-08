/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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

import org.openrdf.rio.RDFParserFactory;
import org.openrdf.rio.RDFWriterFactory;
import org.openrdf.rio.n3.N3ParserFactory;
import org.openrdf.rio.n3.N3WriterFactory;


/**
 * <p>
 *  I/O binding for N3. Based on Sesame RIO.
 * </p>
 *
 * <p>
 * 	Created Dec 7, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class N3Binding extends AbstractRioBinding {
    
    /**
     * Creates a new RDF binding instance.
     */
    public N3Binding() {
    }
    
    // ----------------------------------------------------

	/** 
	* {@inheritDoc}
	*/
	@Override
	protected RDFWriterFactory writerFactory() {
		return new N3WriterFactory();
	}

	/** 
	* {@inheritDoc}
	*/
	@Override
	protected RDFParserFactory parserFactory() {
		return new N3ParserFactory();
	}
    
}
