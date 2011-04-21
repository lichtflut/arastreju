/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

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

	/* (non-Javadoc)
	 * @see org.arastreju.sge.io.SemanticGraphIO#read(java.io.InputStream)
	 */
	public SemanticGraph read(final InputStream in) throws IOException,
			OntologyIOException {
		throw new NotYetImplementedException();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.io.SemanticGraphIO#write(org.arastreju.sge.model.SemanticGraph, java.io.OutputStream)
	 */
	public void write(final SemanticGraph graph, final OutputStream out)
			throws IOException, OntologyIOException {
		
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
	
	protected void renderEdges(final StringBuilder sb, final Set<Association> assocs) {
		boolean first = true;
		for (Association assoc : assocs) {
			if (assoc.getClient().isResourceNode()){
				if (first){
					first = false;
				} else {
					sb.append(",");
				}
				sb.append("{\n");
				sb.append("  nodeFrom: '" + assoc.getSupplier().getQualifiedName().toURI() + "', ");
				sb.append("  nodeTo: '" + assoc.getClient().asResource().getQualifiedName().toURI() + "', ");
				sb.append("}\n");
			}
		}
	}

}
