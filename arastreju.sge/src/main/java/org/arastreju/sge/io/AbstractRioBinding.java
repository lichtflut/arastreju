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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFParserFactory;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.RDFWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Abstract base class for Sesame based IO classes.
 * </p>
 *
 * <p>
 * 	Created Dec 7, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractRioBinding implements SemanticGraphIO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRioBinding.class);
    
    // -----------------------------------------------------
    
     public void read(final InputStream in, ReadStatementListener listener) throws IOException, SemanticIOException {
         RDFParser parser = parserFactory().getParser();
         try {
             RdfReadHandler handler = new RdfReadHandler(listener);
             parser.setRDFHandler(handler);
             parser.parse(in, "void");
         } catch (RDFParseException e) {
             throw new SemanticIOException(ErrorCodes.GRAPH_READ_ERROR, "error while reading RDF", e);
         } catch (RDFHandlerException e) {
             throw new SemanticIOException(ErrorCodes.GRAPH_READ_ERROR, "error while reading RDF", e);
         }
     }
    
    public SemanticGraph read(final InputStream in) throws IOException, SemanticIOException {
        final StatementCollector collector = new StatementCollector();
        read(in, collector);
        return collector.toSemanticGraph();
    }
    
    public void write(final SemanticGraph graph, final OutputStream out)
            throws IOException, SemanticIOException {
        try {
            final RDFWriter writer = writerFactory().getWriter(out);
            final NamespaceMap nsMap = new NamespaceMap(graph.getNamespaces());
            LOGGER.debug("PrefixMap: \n" + nsMap);
            for(String prefix : nsMap.getPrefixes()){
                writer.handleNamespace(prefix, nsMap.getNamespace(prefix).getUri());
            }
            
            writer.startRDF();
            for (Statement stmt : graph.getStatements()) {
                writer.handleStatement(new RioStatement(stmt));
            }
            writer.endRDF();
            
        } catch(IllegalArgumentException e){
            throw new SemanticIOException(ErrorCodes.GRAPH_WRITE_ERROR, "associations couldn't be written", e);
        } catch(RDFHandlerException e){
            throw new SemanticIOException(ErrorCodes.GRAPH_WRITE_ERROR, "associations couldn't be written", e);
        }
    }

    public void write(final StatementContainer container, final OutputStream out)
            throws IOException, SemanticIOException {
        try {
            final RDFWriter writer = writerFactory().getWriter(out);
            final NamespaceMap nsMap = new NamespaceMap(container.getNamespaces());
            LOGGER.debug("PrefixMap: \n" + nsMap);
            for(String prefix : nsMap.getPrefixes()){
                writer.handleNamespace(prefix, nsMap.getNamespace(prefix).getUri());
            }

            writer.startRDF();
            for (Statement stmt : container) {
                writer.handleStatement(new RioStatement(stmt));
            }
            writer.endRDF();

        } catch(IllegalArgumentException e){
            throw new SemanticIOException(ErrorCodes.GRAPH_WRITE_ERROR, "associations couldn't be written", e);
        } catch(RDFHandlerException e){
            throw new SemanticIOException(ErrorCodes.GRAPH_WRITE_ERROR, "associations couldn't be written", e);
        }
    }
    
    // ----------------------------------------------------
    
    protected abstract RDFWriterFactory writerFactory();
    
    protected abstract RDFParserFactory parserFactory();

}
