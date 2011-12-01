/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import java.util.HashSet;
import java.util.Set;

import org.arastreju.bindings.neo4j.ArasRelTypes;
import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.NeoAssociation;
import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.bindings.neo4j.extensions.SNValueNeo;
import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.bindings.neo4j.tx.TxAction;
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Handler for resolving, adding and removing of a node's association.
 * </p>
 *
 * <p>
 * 	Created Dec 1, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class AssociationHandler implements NeoConstants {

	private final Logger logger = LoggerFactory.getLogger(AssociationHandler.class);
	
	private final Inferencer inferencer = new NeoInferencer();
	
	private final NeoResourceResolver resolver;
	
	private final ResourceIndex index;

	private final TxProvider txProvider;

	private final ContextAccess ctxAccess;
	
	// ----------------------------------------------------
	
	/**
	 * Creates a new association handler.
	 * @param resolver
	 * @param index
	 * @param txProvider
	 */
	public AssociationHandler(final NeoResourceResolver resolver, final ResourceIndex index, final TxProvider txProvider) {
		this.resolver = resolver;
		this.txProvider = txProvider;
		this.index = index;
		this.ctxAccess = new ContextAccess(resolver);
	}
	
	// ----------------------------------------------------

	/**
	 * Resolve the associations of given association keeper.
	 * @param keeper The association keeper to be resolved.
	 */
	public void resolveAssociations(final NeoAssociationKeeper keeper) {
		for(Relationship rel : keeper.getNeoNode().getRelationships(Direction.OUTGOING)){
			SemanticNode object = null;
			if (rel.isType(ArasRelTypes.REFERENCE)){
				object = resolver.resolve(rel.getEndNode());	
			} else if (rel.isType(ArasRelTypes.VALUE)){
				object = new SNValueNeo(rel.getEndNode());
			}
			final ResourceNode predicate = resolver.findResource(new QualifiedName(rel.getProperty(PREDICATE_URI).toString()));
			final Context[] ctx = ctxAccess.getContextInfo(rel);
			keeper.addAssociationDirectly(new NeoAssociation(keeper.getArasNode(), predicate, object, ctx));
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
			inferencer.addInferenced(stmt, inferenced);
		}
		txProvider.doTransacted(new TxAction() {
			public void execute() {
				createRelationships(keeper.getNeoNode(), statements);
			}
		});
	}
	
	/**
	 * Remove the given association.
	 * @param keeper The keeper.
	 * @param assoc The association.
	 * @return true if the association has been removed.
	 */
	public boolean removeAssociation(final NeoAssociationKeeper keeper, final Association assoc) {
		final Relationship relationship = findCorresponding(keeper.getNeoNode(), assoc);
		if (relationship != null) {
			txProvider.doTransacted(new TxAction() {
				public void execute() {
					index.removeFromIndex(relationship);
					logger.info("Deleting: " + assoc);
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
	
	private void createRelationships(Node subject, Statement... statments) {
		for (Statement stmt : statments) {
			resolver.resolve(stmt.getPredicate());
			if (stmt.getObject().isResourceNode()){
				final ResourceNode arasClient = resolver.resolve(stmt.getObject().asResource());
				final Node neoClient = AssocKeeperAccess.getNeoNode(arasClient);
				createRelationShip(subject, neoClient, stmt);
			} else {
				final Node neoClient = subject.getGraphDatabase().createNode();
				final ValueNode value = stmt.getObject().asValue();
				neoClient.setProperty(PROPERTY_DATATYPE, value.getDataType().name());
				neoClient.setProperty(PROPERTY_VALUE, value.getStringValue());
				createRelationShip(subject, neoClient, stmt);
			}
			index.index(subject, stmt);
		}
	}
	
	private void createRelationShip(final Node subject, final Node object, final Statement stmt) {
		final RelationshipType type = stmt.getObject().isResourceNode() ? ArasRelTypes.REFERENCE : ArasRelTypes.VALUE;
		final Relationship relationship = subject.createRelationshipTo(object, type);
		relationship.setProperty(PREDICATE_URI, SNOPS.uri(stmt.getPredicate()));
		ctxAccess.assignContext(relationship, stmt.getContexts());
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
	
}
