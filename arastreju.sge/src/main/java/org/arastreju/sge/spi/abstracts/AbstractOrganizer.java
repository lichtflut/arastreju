/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.spi.abstracts;

import static org.arastreju.sge.SNOPS.assure;
import static org.arastreju.sge.SNOPS.singleObject;
import static org.arastreju.sge.SNOPS.string;

import org.arastreju.sge.Organizer;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.naming.SimpleNamespace;

/**
 * <p>
 *  Abstract base for {@link Organizer} implementations.
 * </p>
 *
 * <p>
 * 	Created Nov 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class AbstractOrganizer implements Organizer {

	protected Namespace createNamespace(final ResourceNode node) {
		final String uri = string(singleObject(node, Aras.HAS_URI));
		final String prefix = string(singleObject(node, Aras.HAS_PREFIX));
		return new SimpleNamespace(uri, prefix);
	}
	
	protected ResourceNode createNamespaceNode(final Namespace namespace) {
		final SNResource node = new SNResource();
		assure(node, RDF.TYPE, Aras.NAMESPACE);
		assure(node, Aras.HAS_URI, new SNText(namespace.getUri()));
		assure(node, Aras.HAS_PREFIX, new SNText(namespace.getPrefix()));
		return node;
	}
	
	protected Context createContext(final ResourceNode node) {
		return new SimpleContextID(node.getQualifiedName());
	}
	
	protected ResourceNode createContextNode(final ResourceID ctx) {
		return createContextNode(ctx.getQualifiedName());
	}
	
	protected ResourceNode createContextNode(final QualifiedName qn) {
		final SNResource node = new SNResource(qn);
		assure(node, RDF.TYPE, Aras.CONTEXT);
		return node;
	}
	
}
