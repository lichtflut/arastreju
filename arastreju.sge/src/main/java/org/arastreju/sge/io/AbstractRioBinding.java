/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    
    private final Logger logger = LoggerFactory.getLogger(AbstractRioBinding.class);
    
    // -----------------------------------------------------
    
   /**
    * {@inheritDoc}
    */
    public SemanticGraph read(final InputStream in) throws IOException, OntologyIOException {
        final AssociationCollector collector = new AssociationCollector();
        RDFParser parser = parserFactory().getParser();
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
    
    /**
     * {@inheritDoc}
     */
    public void write(final SemanticGraph graph, final OutputStream out)
            throws IOException, OntologyIOException {
        try {
            final RDFWriter writer = writerFactory().getWriter(out);
            
            final NamespaceMap nsMap = new NamespaceMap(graph.getNamespaces());
            logger.debug("PrefixMap: \n" + nsMap);
            for(String prefix : nsMap.getPrefixes()){
                writer.handleNamespace(prefix, nsMap.getNamespace(prefix).getUri());
            }
            
            writer.startRDF();
            for (Statement stmt : graph.getStatements()) {
                writer.handleStatement(new RioStatement(stmt));
            }
            writer.endRDF();
            
        } catch(IllegalArgumentException e){
            throw new OntologyIOException("associations couldn't be written", e);
        } catch(RDFHandlerException e){
            throw new OntologyIOException("associations couldn't be written", e);
        }
    }
    
    // ----------------------------------------------------
    
    protected abstract RDFWriterFactory writerFactory();
    
    protected abstract RDFParserFactory parserFactory();

}
