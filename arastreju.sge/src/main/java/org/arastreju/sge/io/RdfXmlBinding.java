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
import java.io.InputStream;
import java.io.OutputStream;

import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.associations.Association;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.rdfxml.RDFXMLParserFactory;
import org.openrdf.rio.rdfxml.RDFXMLWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * 	I/O binding for RDF XML. Based on Sesame RIO.
 * </p>
 * 
 * <p>
 *	Created: 09.06.2009
 * </p>
 *
 * @author Oliver Tigges 
 */
public class RdfXmlBinding implements SemanticGraphIO {
	
	private final Logger logger = LoggerFactory.getLogger(RdfXmlBinding.class);
	
	// -----------------------------------------------------
	
	/**
	 * Creates a new RDF binding instance.
	 */
	public RdfXmlBinding() {
	}
	
	// ------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.io.SemanticGraphIO#read(java.io.InputStream)
	 */
	public SemanticGraph read(final InputStream in) throws IOException, OntologyIOException {
		final AssociationCollector collector = new AssociationCollector();
		RDFParser parser = new RDFXMLParserFactory().getParser();
		try {
			RdfReadHandler handler = new RdfReadHandler(collector);
			parser.setRDFHandler(handler);
			parser.parse(in, "void");
			return collector.toSemanticGraph();
		} catch (RDFParseException e) {
			throw new OntologyIOException("error while reading RDF", e);
		} catch (RDFHandlerException e) {
			throw new OntologyIOException("error while reading RDF", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.io.SemanticGraphIO#write(org.arastreju.sge.model.SemanticGraph, java.io.OutputStream)
	 */
	public void write(final SemanticGraph graph, final OutputStream out)
			throws IOException, OntologyIOException {
		try {
			final RDFWriter writer = new RDFXMLWriterFactory().getWriter(out);
			
			final NamespaceMap nsMap = new NamespaceMap(graph.getNamespaces());
			logger.debug("PrefixMap: \n" + nsMap);
			for(String prefix : nsMap.getPrefixes()){
				writer.handleNamespace(prefix, nsMap.getNamespace(prefix).getUri());
			}
			
			writer.startRDF();
			for (Association assoc : graph.getAssociations()) {
				writer.handleStatement(new RioStatement(assoc));
			}
			writer.endRDF();
			
		} catch(IllegalArgumentException e){
			throw new OntologyIOException("associations couldn't be written", e);
		} catch(RDFHandlerException e){
			throw new OntologyIOException("associations couldn't be written", e);
		}
	}
	
}
