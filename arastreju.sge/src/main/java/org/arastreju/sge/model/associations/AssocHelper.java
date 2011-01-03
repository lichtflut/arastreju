/*
 * Copyright (C) 2010 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.model.associations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * Helper class for associations.
 * 
 * Created: 19.06.2009
 *
 * @author Oliver Tigges 
 */
public class AssocHelper {
	
	public static List<SemanticNode> getClients(final Collection<Association> assocs){
		final List<SemanticNode> result = new ArrayList<SemanticNode>(assocs.size());
		for (Association assoc : assocs) {
			result.add(assoc.getClient());
		}
		return result;
	}
	
	public static List<ResourceNode> getSuppliers(final Collection<Association> assocs){
		final List<ResourceNode> result = new ArrayList<ResourceNode>(assocs.size());
		for (Association assoc : assocs) {
			result.add(assoc.getSupplier());
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
			if (!objects.contains(assoc.getClient())){
				subject.revoke(assoc);
			} else {
				existing.add(assoc.getClient());
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
