/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j;

import static org.arastreju.sge.SNOPS.assure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.arastreju.bindings.neo4j.impl.SemanticNetworkAccess;
import org.arastreju.bindings.neo4j.query.NeoQueryBuilder;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.naming.Namespace;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.naming.SimpleNamespace;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;
import org.arastreju.sge.spi.abstracts.AbstractOrganizer;

/**
 * <p>
 *  Neo implementation of Organizer.
 * </p>
 *
 * <p>
 * 	Created Sep 22, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoOrganizer extends AbstractOrganizer {

	private final SemanticNetworkAccess sna;
	
	// -----------------------------------------------------
	
	/**
	 * Constructor.
	 * @param sna The semantic network access.
	 */
	public NeoOrganizer(final SemanticNetworkAccess sna) {
		this.sna = sna;
	}
	
	// -----------------------------------------------------
	
	/** 
	 * {@inheritDoc}
	 */
	public Collection<Namespace> getNamespaces() {
		final List<Namespace> result = new ArrayList<Namespace>();
		final List<ResourceNode> nodes = sna.getIndex().lookup(RDF.TYPE, Aras.NAMESPACE).toList();
		for (ResourceNode node : nodes) {
			result.add(createNamespace(node));
		}
		return result;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public Namespace registerNamespace(final String uri, final String prefix) {
		final Query query = query()
				.addField(RDF.TYPE, Aras.NAMESPACE)
				.and()
				.addField(Aras.HAS_URI, uri);
		final QueryResult result = query.getResult();
		if (!result.isEmpty()) {
			final ResourceNode node = sna.resolve(result.iterator().next());
			assure(node,  Aras.HAS_PREFIX, new SNText(prefix));
			return new SimpleNamespace(uri, prefix);
		} else {
			final Namespace ns = new SimpleNamespace(uri, prefix);
			final ResourceNode node = createNamespaceNode(ns);
			sna.attach(node);
			return ns;
		}
	}
	
	// -----------------------------------------------------

	/** 
	 * {@inheritDoc}
	 */
	public Collection<Context> getContexts() {
		final List<Context> result = new ArrayList<Context>();
		final List<ResourceNode> nodes = sna.getIndex().lookup(RDF.TYPE, Aras.CONTEXT).toList();
		for (ResourceNode node : nodes) {
			result.add(createContext(node));
		}
		return result;
	}

	/** 
	 * {@inheritDoc}
	 */
	public Context registerContext(final QualifiedName qn) {
		final ResourceNode node = createContextNode(qn);
		sna.attach(node);
		return new SimpleContextID(qn);
	}
	
	
	// ----------------------------------------------------
	
	private Query query() {
		return new NeoQueryBuilder(sna.getIndex());
	}
	
}
