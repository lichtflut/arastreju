/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.associations.Association;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

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

	public static String uri(final ResourceID rid){
		return rid.getQualifiedName().toURI();
	}
	
	public static String string(final SemanticNode node) {
		if (node == null) {
			return null;
		} else if (node.isValueNode()) {
			return node.asValue().getStringValue();
		} else {
			return node.asResource().getQualifiedName().toString();
		}
	}
	
	// -- ASSOCIATION -------------------------------------
	
	public static List<SemanticNode> objects(final Collection<Association> assocs){
		final List<SemanticNode> result = new ArrayList<SemanticNode>(assocs.size());
		for (Association assoc : assocs) {
			result.add(assoc.getObject());
		}
		return result;
	}
	
	public static List<ResourceNode> subjects(final Collection<Association> assocs){
		final List<ResourceNode> result = new ArrayList<ResourceNode>(assocs.size());
		for (Association assoc : assocs) {
			result.add(assoc.getSubject());
		}
		return result;
	}
	
	/**
	 * Replaces all associations for given subject and predicate by a new one using the given object.
	 * @param ctx The context.
	 * @param subject
	 * @param predicate
	 * @param object
	 */
	public static void replace(final Context ctx, final ResourceNode subject, final ResourceID predicate, final SemanticNode object){
		for(Association assoc: subject.getAssociations(predicate)){
			subject.revoke(assoc);
		}
		Association.create(subject, predicate, object, ctx);
	}
	
	/**
	 * Replaces all associations for given subject and predicate by the new ones.
	 * @param ctx The context.
	 * @param subject
	 * @param predicate
	 * @param objects The collection of objects to be added.
	 */
	public static void replace(final Context ctx, final ResourceNode subject, final ResourceID predicate, final Collection<? extends SemanticNode> objects){
		final List<SemanticNode> existing = new ArrayList<SemanticNode>();
		// 1st: remove no longer existing
		for(Association assoc: subject.getAssociations(predicate)){
			if (!objects.contains(assoc.getObject())){
				subject.revoke(assoc);
			} else {
				existing.add(assoc.getObject());
			}
		}
		// 2nd: add not yet existing
		for (SemanticNode current: objects){
			if (!existing.contains(objects)){
				Association.create(subject, predicate, current, ctx);
			}
		}
	}
	
}
