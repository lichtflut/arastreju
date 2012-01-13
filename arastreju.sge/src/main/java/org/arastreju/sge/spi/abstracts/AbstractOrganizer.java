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
import org.arastreju.sge.model.nodes.views.SNBoolean;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.naming.SimpleNamespace;
import org.arastreju.sge.security.Domain;
import org.arastreju.sge.security.impl.DomainImpl;

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
	
	protected Domain createDomain(ResourceNode node) {
		return new DomainImpl(node);
	}
	
	protected ResourceNode createMasterDomainNode(String name) {
		final ResourceNode node = new SNResource();
		node.addAssociation(Aras.HAS_UNIQUE_NAME, new SNText(name), Aras.IDENT);
		node.addAssociation(Aras.IS_MASTER_DOMAIN, new SNBoolean(true), Aras.IDENT);
		node.addAssociation(RDF.TYPE, Aras.DOMAIN, Aras.IDENT);
		return node;
	}
	
	protected ResourceNode createDomainNode(String name, String title, String description) {
		final ResourceNode node = new SNResource();
		node.addAssociation(Aras.HAS_UNIQUE_NAME, new SNText(name), Aras.IDENT);
		if (title != null) {
			node.addAssociation(Aras.HAS_TITLE, new SNText(title), Aras.IDENT);
		}
		if (description != null) {
			node.addAssociation(Aras.HAS_DESCRIPTION, new SNText(description), Aras.IDENT);
		}
		node.addAssociation(RDF.TYPE, Aras.DOMAIN, Aras.IDENT);
		return node;
	}
}
