/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import java.util.HashSet;
import java.util.Set;

import org.arastreju.bindings.neo4j.ArasRelTypes;
import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.bindings.neo4j.extensions.SNValueNeo;
import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.bindings.neo4j.mapping.NodeMapper;
import org.arastreju.bindings.neo4j.tx.TxAction;
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Dec 1, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class AssociationHandler implements NeoConstants {
	
	private final NeoResourceResolver resolver;
	
	private final Logger logger = LoggerFactory.getLogger(AssociationHandler.class);

	private final ResourceIndex index;

	private final TxProvider txProvider;

	private final GraphDatabaseService gdbService;
	
	// ----------------------------------------------------
	
	public AssociationHandler(final GraphDatabaseService gdbService, final NeoResourceResolver resolver, final TxProvider txProvider, final ResourceIndex index) {
		this.gdbService = gdbService;
		this.resolver = resolver;
		this.txProvider = txProvider;
		this.index = index;
	}
	
	// ----------------------------------------------------

	public void resolveAssociations(final NeoAssociationKeeper keeper) {
		for(Relationship rel : keeper.getNeoNode().getRelationships(Direction.OUTGOING)){
			SemanticNode object = null;
			if (rel.isType(ArasRelTypes.REFERENCE)){
				object = resolver.resolve(rel.getEndNode());	
			} else if (rel.isType(ArasRelTypes.VALUE)){
				object = new SNValueNeo(rel.getEndNode());
			}
			final ResourceNode predicate = resolver.findResource(new QualifiedName(rel.getProperty(PREDICATE_URI).toString()));
			final Context[] ctx = new ContextAccess(resolver).getContextInfo(rel);
			keeper.addResolvedAssociation(keeper.getArasNode(), predicate, object, ctx);
		}
	}
	
	/**
	 * Add a new Association to given Neo node, or rather create a corresponding Relation.
	 * @param subject The neo node, which shall be the subject in the new Relation.
	 * @param stmt The Association.
	 */
	public void addAssociation(final NeoAssociationKeeper keeper, final Statement... statements) {
		final Set<Statement> inferenced = new HashSet<Statement>();
		for (Statement stmt : statements) {
			new NeoInferencer().addInferenced(stmt, inferenced);
			inferenced.add(stmt);
		}
		txProvider.doTransacted(new TxAction() {
			public void execute() {
				createRelationships(keeper.getNeoNode(), inferenced);
			}
		});
	}
	
	public boolean removeAssociation(final NeoAssociationKeeper keeper, final Association assoc) {
		final Relationship relationship = findCorresponding(keeper.getNeoNode(), assoc);
		if (relationship != null) {
			txProvider.doTransacted(new TxAction() {
				public void execute() {
					index.removeFromIndex(relationship);
					logger.warn("Deleting: " + assoc);
					relationship.delete();
				}
			});
			return true;
		} else {
			logger.warn("Didn't find corresponding relationship to delete: " + assoc);
			return false;	
		}
	}
	
	// ----------------------------------------------------
	
	private void createRelationships(Node subject, Set<Statement> statments) {
		for (Statement stmt : statments) {
			resolver.resolve(stmt.getPredicate());
			if (stmt.getObject().isResourceNode()){
				final ResourceNode arasClient = resolver.resolve(stmt.getObject().asResource());
				final Node neoClient = AssocKeeperAccess.getNeoNode(arasClient);
				createRelationShip(subject, neoClient, stmt);
			} else {
				final Node neoClient = gdbService.createNode();
				new NodeMapper(resolver).toNeoNode(stmt.getObject().asValue(), neoClient);
				createRelationShip(subject, neoClient, stmt);
			}
			index.index(subject, stmt);
		}
	}
	
	private void createRelationShip(final Node subject, final Node object, final Statement stmt) {
		final RelationshipType type = stmt.getObject().isResourceNode() ? ArasRelTypes.REFERENCE : ArasRelTypes.VALUE;
		final Relationship relationship = subject.createRelationshipTo(object, type);
		relationship.setProperty(PREDICATE_URI, SNOPS.uri(stmt.getPredicate()));
		assignContext(relationship, stmt.getContexts());
		logger.debug("added relationship--> " + relationship + " to node " + subject);
	}
	
	protected Relationship findCorresponding(final Node neoNode, final Statement stmt) {
		final String assocPredicate = stmt.getPredicate().getQualifiedName().toURI();
		final String assocValue = SNOPS.string(stmt.getObject());
		for(Relationship rel : neoNode.getRelationships(Direction.OUTGOING)) {
			final String predicate = (String) rel.getProperty(PREDICATE_URI);
			if (assocPredicate.equals(predicate)) {
				if (stmt.getObject().isResourceNode()) {
					final String uri = (String) rel.getEndNode().getProperty(PROPERTY_URI);
					if (assocValue.equals(uri)) {
						return rel;
					}
				} else {
					final String value = (String) rel.getEndNode().getProperty(PROPERTY_VALUE);
					if (assocValue.equals(value)) {
						return rel;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Assigns context information to a relationship.
	 * @param relationship The relationship to be assigned to the contexts.
	 * @param contexts The contexts.
	 */
	protected void assignContext(final Relationship relationship, final Context[] contexts) {
		new ContextAccess(resolver).assignContext(relationship, contexts);
	}

}
