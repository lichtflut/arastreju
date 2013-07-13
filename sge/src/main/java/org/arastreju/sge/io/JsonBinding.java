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

import org.arastreju.sge.eh.meta.NotYetImplementedException;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Feb 1, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class JsonBinding implements SemanticGraphIO {

	@Override
	public void read(InputStream in, ReadStatementListener listener) throws IOException, SemanticIOException {
		throw new NotYetImplementedException();
	}

    @Override
	public SemanticGraph read(final InputStream in) throws IOException,
			SemanticIOException {
		throw new NotYetImplementedException();
	}
	
	// ----------------------------------------------------

	@Override
	public void write(final SemanticGraph graph, final OutputStream out)
			throws IOException, SemanticIOException {
		
		boolean first = true;
		for (final ResourceNode subject : graph.getSubjects()) {
			final StringBuilder sb = new StringBuilder();
			if (first){
				first = false;
			} else {
				sb.append(",");
			}
			sb.append("{");
			sb.append("  id: \"" + subject.getQualifiedName().toURI() + "\", \n");
			sb.append("  name: \"" + subject.getQualifiedName().getSimpleName()+ "\", \n");
			sb.append("  adjacencies: [\n");
			renderEdges(sb, subject.getAssociations());
			sb.append("  ]\n");
			sb.append("}\n");
			out.write(sb.toString().getBytes());
		}
	}

    @Override
    public void write(StatementContainer container, OutputStream out) {
        throw new NotYetImplementedException();
    }

    protected void renderEdges(final StringBuilder sb, final Set<? extends Statement> assocs) {
		boolean first = true;
		for (Statement assoc : assocs) {
			if (assoc.getObject().isResourceNode()){
				if (first){
					first = false;
				} else {
					sb.append(",");
				}
				sb.append("{\n");
				sb.append("  nodeFrom: '" + assoc.getSubject().getQualifiedName().toURI() + "', ");
				sb.append("  nodeTo: '" + assoc.getObject().asResource().getQualifiedName().toURI() + "', ");
				sb.append("}\n");
			}
		}
	}

}
