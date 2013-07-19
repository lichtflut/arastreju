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
package org.arastreju.sge.organize;

import org.arastreju.sge.ArastrejuGate;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.ContextID;
import org.arastreju.sge.io.StatementContainer;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.views.SNContext;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.naming.SimpleNamespace;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.arastreju.sge.SNOPS.assure;
import static org.arastreju.sge.SNOPS.singleObject;
import static org.arastreju.sge.SNOPS.string;

/**
 * <p>
 *  Organizer for namespaces and contexts.
 * </p>
 *
 * <p>
 * 	Created Nov 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class Organizer {

    private final ArastrejuGate gate;

    // ----------------------------------------------------

    public Organizer(ArastrejuGate gate) {
        this.gate = gate;
    }

    // ----------------------------------------------------

    public Collection<Namespace> getNamespaces() {
        final List<Namespace> result = new ArrayList<Namespace>();
        final Query query = conversation().createQuery().addField(RDF.TYPE, Aras.NAMESPACE);
        for (ResourceNode node : query.getResult()) {
            result.add(createNamespace(node));
        }
        return result;
    }

    public Collection<Context> getContexts() {
        final List<Context> result = new ArrayList<Context>();
        final Query query = conversation().createQuery().addField(RDF.TYPE, Aras.CONTEXT);
        for (ResourceNode node : query.getResult()) {
            result.add(ContextID.forContext(node.getQualifiedName()));
        }
        return result;
    }

    // ----------------------------------------------------

    public Namespace registerNamespace(final String uri, final String prefix) {
        try (Conversation conversation = conversation()) {
            final Query query = conversation.createQuery()
                    .addField(RDF.TYPE, Aras.NAMESPACE)
                    .and()
                    .addField(Aras.HAS_URI, uri);
            final QueryResult result = query.getResult();
            if (!result.isEmpty()) {
                final ResourceNode node = conversation.resolve(result.iterator().next());
                assure(node, Aras.HAS_PREFIX, new SNText(prefix));
                return new SimpleNamespace(uri, prefix);
            } else {
                final Namespace ns = new SimpleNamespace(uri, prefix);
                final ResourceNode node = createNamespaceNode(ns);
                conversation.attach(node);
                return ns;
            }
        }

    }

    public Context findContext(QualifiedName qn) {
        ResourceNode node = conversation().findResource(qn);
        if (node == null) {
            return null;
        } else if (SNOPS.objectsAsResources(node, RDF.TYPE).contains(Aras.CONTEXT)) {
            return SNContext.from(node);
        } else {
            throw new IllegalArgumentException("Node with given qualified name is not a context.");
        }
    }

    public Context registerContext(final QualifiedName qn) {
        final SNContext context = new SNContext(qn);
        conversation().attach(context);
        return context;
    }

    // ----------------------------------------------------

    public StatementContainer getStatements(final Context ctx) {
        return new StatementContainer() {
            @Override
            public Collection<Namespace> getNamespaces() {
                return Organizer.this.getNamespaces();
            }

            @Override
            public Iterator<Statement> iterator() {
                final Conversation conversation = conversation(ctx);
                conversation.getConversationContext().setStrictContextRegarding(true);
                final QueryResult queryResult = conversation.createQuery().addURI("*").getResult();
                final Iterator<ResourceNode> nodeIterator = queryResult.iterator();
                return new StatementIterator(nodeIterator);
            }
        };
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
	
    // ----------------------------------------------------

    protected Conversation conversation() {
        return gate.startConversation();
    }

    protected Conversation conversation(final Context readContext) {
        Conversation conversation = gate.startConversation();
        conversation.getConversationContext().setPrimaryContext(readContext);
        return conversation;
    }

    // ----------------------------------------------------

    private static class StatementIterator implements Iterator<Statement> {

        private Iterator<Statement> stmtIterator;
        private final Iterator<ResourceNode> nodeIterator;

        public StatementIterator(Iterator<ResourceNode> nodeIterator) {
            this.nodeIterator = nodeIterator;
        }

        @Override
        public boolean hasNext() {
            if (stmtIterator == null) {
                forwardToNextResourceNode();
            }
            while (stmtIterator != null) {
                if (stmtIterator.hasNext()) {
                    return true;
                } else {
                    forwardToNextResourceNode();
                }

            }
            return false;
        }

        @Override
        public Statement next() {
            return stmtIterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void forwardToNextResourceNode() {
            while (nodeIterator.hasNext()) {
                ResourceNode node = nodeIterator.next();
                if (node != null) {
                    stmtIterator = node.getAssociations().iterator();
                    return;
                }
            }

            stmtIterator = null;
        }
    }

}
