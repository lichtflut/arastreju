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

import org.arastreju.sge.model.SemanticGraph;

/**
 * <p>
 *  Base interface for input and output of semantic graphs.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface SemanticGraphIO {
	
	/**
	 * Reads statements in streaming mode.
	 * @param in The input stream.
	 * @param listener The listener for read statements.
	 * @throws IOException
	 * @throws SemanticIOException
	 */
	void read(InputStream in, ReadStatementListener listener) 
			throws IOException, SemanticIOException;

	/**
	 * Reads a model extract from an RDS input stream.
	 * @param in The input stream containing the model.
	 * @return The model extract read from inpuit stream.
	 * @throws IOException
	 * @throws SemanticIOException
	 */
	SemanticGraph read(InputStream in) throws IOException,
			SemanticIOException;
	
	// ----------------------------------------------------

	/**
	 * Writes the extract as RDF to given stream.
	 * @param graph The graph to write.
	 * @param out The output stream.
	 */
	void write(SemanticGraph graph, OutputStream out)
			throws IOException, SemanticIOException;


    /**
     * Writed statments from given provider to output stream.
     * @param provider The provider of statements.
     * @param out The output stream.
     */
    void write(final StatementProvider provider, final OutputStream out)
            throws IOException, SemanticIOException;
}