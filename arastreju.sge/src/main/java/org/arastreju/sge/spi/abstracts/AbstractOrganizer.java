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

import org.arastreju.sge.Conversation;
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
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;

import static org.arastreju.sge.SNOPS.assure;
import static org.arastreju.sge.SNOPS.singleObject;
import static org.arastreju.sge.SNOPS.string;

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

    public Namespace registerNamespace(final String uri, final String prefix) {
        final Conversation conversation = conversation();
        final Query query = conversation.createQuery()
                .addField(RDF.TYPE, Aras.NAMESPACE)
                .and()
                .addField(Aras.HAS_URI, uri);
        final QueryResult result = query.getResult();
        if (!result.isEmpty()) {
            final ResourceNode node = conversation.resolve(result.iterator().next());
            assure(node,  Aras.HAS_PREFIX, new SNText(prefix));
            return new SimpleNamespace(uri, prefix);
        } else {
            final Namespace ns = new SimpleNamespace(uri, prefix);
            final ResourceNode node = createNamespaceNode(ns);
            conversation.attach(node);
            return ns;
        }
    }

    public Context registerContext(final QualifiedName qn) {
        final ResourceNode node = createContextNode(qn);
        conversation().attach(node);
        return new SimpleContextID(qn);
    }

    // ----------------------------------------------------

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

    // ----------------------------------------------------

    protected abstract Conversation conversation();

}
