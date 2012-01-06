/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;

import de.lichtflut.infra.Infra;

/**
 * <p>
 *  Utility class with static mehtods for Semantic Network OPerationS.
 * </p>
 *
 * <p>
 * 	Created Apr 29, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class SNOPS {
	
	// -- TRANSFORMATION ----------------------------------

	public static String uri(final ResourceID rid){
		return rid.getQualifiedName().toURI();
	}
	
	public static QualifiedName qualify(final String uri){
		return new QualifiedName(uri);
	}
	
	public static ResourceID id(final QualifiedName qn){
		return new SimpleResourceID(qn);
	}
	
	/**
	 * @param node The node.
	 * @return An isolated ID object.
	 */
	public static ResourceID id(final ResourceNode node) {
		return new SimpleResourceID(node);
	}
	
	/**
	 * Creates a string representation for given node. For a resource node this
	 * will be the URI.
	 * @param node The node.
	 * @return The string representation.
	 */
	public static String string(final SemanticNode node) {
		if (node == null) {
			return null;
		} else if (node.isValueNode()) {
			return node.asValue().getStringValue();
		} else {
			return node.asResource().getQualifiedName().toString();
		}
	}
	
	// -- ASSOCIATIONS -------------------------------------
	
	/**
	 * Fetch the first association corresponding to given predicate.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @return The first matching association or null.
	 */
	public static Statement fetchAssociation(final ResourceNode subject, final ResourceID predicate) {
		Set<Statement> associations = subject.getAssociations(predicate);
		if (associations.isEmpty()) {
			return null;
		} else {
			return associations.iterator().next();
		}
	}
	
	/**
	 * Fetch the first association's object corresponding to given predicate.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @return The first matching object or null.
	 */
	public static SemanticNode fetchObject(final ResourceNode subject, final ResourceID predicate) {
		Set<Statement> associations = subject.getAssociations(predicate);
		if (associations.isEmpty()) {
			return null;
		} else {
			return associations.iterator().next().getObject();
		}
	}
	
	/**
	 * Fetch the only association corresponding to given predicate.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @return The single association or null.
	 * @throws ArastrejuRuntimeException if more than one association of given predicate present.
	 */
	public static Statement singleAssociation(final ResourceNode subject, final ResourceID predicate) {
		Set<Statement> associations = subject.getAssociations(predicate);
		if (associations.isEmpty()) {
			return null;
		} else if (associations.size() > 1) {
			throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_RUNTIME_ERROR, 
					"Expected only one association with predicate '" + predicate + "' but found: " + associations);
		} else {
			return associations.iterator().next();
		}
	}
	
	/**
	 * Fetch the only association corresponding to given predicate.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @return The single association or null.
	 * @throws ArastrejuRuntimeException if more than one association of given predicate present.
	 */
	public static SemanticNode singleObject(final ResourceNode subject, final ResourceID predicate) {
		final Statement association = singleAssociation(subject, predicate);
		if (association != null) {
			return association.getObject();
		} else {
			return null;
		}
	}
	
	public static Set<ResourceID> subjects(final ResourceNode subject, final ResourceID predicate){
		return subjects(subject.getAssociations(predicate));
	}
	
	public static Set<SemanticNode> objects(final ResourceNode subject, final ResourceID predicate){
		return objects(subject.getAssociations(predicate));
	}
	
	public static Set<ResourceID> predicates(final ResourceNode subject, final ResourceID predicate){
		return predicates(subject.getAssociations(predicate));
	}
	
	public static Set<ResourceID> subjects(final Collection<Statement> assocs){
		final Set<ResourceID> result = new HashSet<ResourceID>(assocs.size());
		for (Statement assoc : assocs) {
			result.add(assoc.getSubject());
		}
		return result;
	}
	
	public static Set<SemanticNode> objects(final Collection<Statement> assocs){
		final Set<SemanticNode> result = new HashSet<SemanticNode>(assocs.size());
		for (Statement assoc : assocs) {
			result.add(assoc.getObject());
		}
		return result;
	}
	
	public static Set<ResourceID> predicates(final Collection<Statement> assocs){
		final Set<ResourceID> result = new HashSet<ResourceID>(assocs.size());
		for (Statement assoc : assocs) {
			result.add(assoc.getPredicate());
		}
		return result;
	}
	
	// -- MODIFICATIONS -----------------------------------
	
	/**
	 * Create a new associated statement, that will be added to the subject. 
	 */
	public static Statement associate(final ResourceNode subject, final ResourceID predicate, final SemanticNode object, final Context... ctx){
		return subject.addAssociation(predicate, object, ctx);
	}
	
	/**
	 * Assures that the subject has only this object for given predicate.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @param object The object to be set.
	 * @param contexts The contexts.
	 */
	public static Statement assure(final ResourceNode subject, final ResourceID predicate, final SemanticNode object, final Context... contexts){
		final Set<Statement> all = subject.getAssociations(predicate);
		if (all.size() > 1) {
			throw new IllegalStateException("replace not possible if more than one associations exists: " + all);
		}
		if (all.isEmpty()) {
			return associate(subject, predicate, object, contexts);	
		} else {
			final Statement existing = all.iterator().next();
			if (!Infra.equals(existing.getObject(), object)) {
				subject.removeAssociation(existing);
				return associate(subject, predicate, object, contexts);
			} else {
				return existing;
			}
		}
	}
	
	/**
	 * Assures that the subject has only the given objects for this predicate.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @param objects The collection of objects to be added.
	 * @param contexts The contexts.
	 */
	public static void assure(final ResourceNode subject, final ResourceID predicate, final Collection<? extends SemanticNode> objects, final Context... contexts){
		final List<SemanticNode> existing = new ArrayList<SemanticNode>();
		// 1st: remove no longer existing
		for(Statement assoc: subject.getAssociations(predicate)){
			if (!objects.contains(assoc.getObject())){
				subject.removeAssociation(assoc);
			} else {
				existing.add(assoc.getObject());
			}
		}
		// 2nd: add not yet existing
		for (SemanticNode current: objects){
			if (!existing.contains(objects)){
				associate(subject, predicate, current, contexts);
			}
		}
	}
	
	/**
	 * Remove all associations of given predicate.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 */
	public static void remove(final ResourceNode subject, ResourceID predicate) {
		for(Statement assoc: subject.getAssociations(predicate)) {
			subject.removeAssociation(assoc);
		}
	}
	
	/**
	 * Remove all associations of given predicate.
	 * @param subject The subject.
	 * @param predicate The predicate.
	 * @param object The object.
	 * @return boolean indicating if a corresponding relation has been found and removed.
	 */
	public static boolean remove(final ResourceNode subject, ResourceID predicate, SemanticNode object) {
		boolean removed = false;
		for(Statement assoc: subject.getAssociations(predicate)) {
			if (assoc.getObject().equals(object)) {
				subject.removeAssociation(assoc);	
				removed = true;
			}
		}
		return removed;
	}

}
