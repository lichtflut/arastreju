/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.arastreju.bindings.neo4j.ArasRelTypes;
import org.arastreju.bindings.neo4j.NeoConstants;
import org.arastreju.bindings.neo4j.extensions.NeoAssociationKeeper;
import org.arastreju.bindings.neo4j.extensions.SNValueNeo;
import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.bindings.neo4j.tx.TxProvider;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.TxAction;
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
	
	private final Inferencer hardInferencer;
	
	private final Inferencer softInferencer;
	
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
		this.softInferencer = new NeoSoftInferencer(resolver);
		this.hardInferencer = new NeoHardInferencer(resolver);
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
			keeper.addAssociationDirectly(new DetachedStatement(keeper.getID(), predicate, object, ctx));
		}
	}
	
	// ----------------------------------------------------
	
	/**
	 * Add a new Association to given Neo node, or rather create a corresponding Relation.
	 * @param subject The neo node, which shall be the subject in the new Relation.
	 * @param stmt The Association.
	 */
	public void addAssociation(final NeoAssociationKeeper keeper, final Statement stmt) {
		txProvider.doTransacted(new TxAction() {
			public void execute() {
				
				final ResourceNode predicate = resolver.resolve(stmt.getPredicate());
				final SemanticNode object = resolve(stmt.getObject());
				final Statement assoc = new DetachedStatement(keeper.getID(), predicate, object, stmt.getContexts());
				keeper.getAssociations().add(assoc);
				
				createRelationships(keeper.getNeoNode(), stmt);
				final List<Statement> stmtList = Collections.singletonList(stmt);
				addHardInferences(keeper, stmtList);
				addSoftInferences(keeper, stmtList);
				
			}
		});
		
	}

	/**
	 * Remove the given association.
	 * @param keeper The keeper.
	 * @param assoc The association.
	 * @return true if the association has been removed.
	 */
	public boolean removeAssociation(final NeoAssociationKeeper keeper, final Statement assoc) {
		final Relationship relationship = findCorresponding(keeper.getNeoNode(), assoc);
		if (relationship != null) {
			txProvider.doTransacted(new TxAction() {
				public void execute() {
					logger.info("Deleting: " + assoc);
					relationship.delete();
					//index.removeFromIndex(keeper.getNeoNode(), assoc);
					removeHardInferences(keeper, Collections.singleton(assoc));
					index.reindex(keeper.getNeoNode(), keeper.getQualifiedName(), keeper.getAssociations());
					addSoftInferences(keeper, keeper.getAssociations());
				}
			});
			return true;
		} else {
			logger.warn("Didn't find corresponding relationship to delete: " + assoc);
			return false;	
		}
	}
	
	// ----------------------------------------------------
	
	private void addSoftInferences(final NeoAssociationKeeper keeper, final Collection<? extends Statement> originals) {
		final Set<Statement> inferenced = new HashSet<Statement>();
		for (Statement stmt : originals) {
			softInferencer.addInferenced(stmt, inferenced);
		}
		for (Statement stmt : inferenced) {
			if (stmt.getSubject().getQualifiedName().equals(keeper.getQualifiedName())) {
				index.index(keeper.getNeoNode(), stmt);
			} else {
				logger.warn("Inferred statement can not be indexed: " + stmt);
			}
		}
	}
	
	private void addHardInferences(final NeoAssociationKeeper keeper, final Collection<? extends Statement> originals) {
		final Set<Statement> inferenced = new HashSet<Statement>();
		for (Statement stmt : originals) {
			hardInferencer.addInferenced(stmt, inferenced);
		}
		for (Statement stmt : inferenced) {
			addStatements(stmt);
		}
	}
	
	private void removeHardInferences(final NeoAssociationKeeper keeper, final Collection<? extends Statement> originals) {
		final Set<Statement> inferenced = new HashSet<Statement>();
		for (Statement stmt : originals) {
			hardInferencer.addInferenced(stmt, inferenced);
		}
		removeStatements(inferenced);
	}
	
	/**
	 * Add new statements.
	 * @param statements The statements.
	 */
	private void addStatements(final Statement... statements) {
		for (Statement stmt : statements) {
			ResourceNode subject = resolver.resolve(stmt.getSubject());
			SNOPS.associate(subject, stmt.getPredicate(), stmt.getObject(), stmt.getContexts());
		}
	}
	
	/**
	 * Remove statements.
	 * @param statements The statements.
	 */
	private void removeStatements(final Collection<? extends Statement> statements) {
		for (Statement stmt : statements) {
			ResourceNode subject = resolver.resolve(stmt.getSubject());
			SNOPS.remove(subject, stmt.getPredicate(), stmt.getObject());
		}
	}
	
	private void createRelationships(Node subject, Statement stmt) {
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
	
	private void createRelationShip(final Node subject, final Node object, final Statement stmt) {
		final RelationshipType type = stmt.getObject().isResourceNode() ? ArasRelTypes.REFERENCE : ArasRelTypes.VALUE;
		final Relationship relationship = subject.createRelationshipTo(object, type);
		relationship.setProperty(PREDICATE_URI, SNOPS.uri(stmt.getPredicate()));
		ctxAccess.assignContext(relationship, stmt.getContexts());
		logger.debug("added relationship--> " + relationship + " to node " + subject);
	}
	
	private Relationship findCorresponding(final Node neoNode, final Statement stmt) {
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
	
	private SemanticNode resolve(final SemanticNode node) {
		if (node.isResourceNode()) {
			return resolver.resolve(node.asResource());
		} else {
			return node;
		}
	}
	
}
