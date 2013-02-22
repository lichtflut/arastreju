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
package org.arastreju.sge.traverse;

import java.util.Collection;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * @author Oliver Tigges
 */
public abstract class ResourceMatcher implements Matcher {

	public static ResourceMatcher equals(final ResourceID rid) {
		return new ResourceMatcher() {
			@Override
			public boolean matches(final ResourceNode node) {
				return node.equals(rid);
			}
		};
	}

	// ----------------------------------------------------

	@Override
	public final boolean matches(final Collection<? extends SemanticNode> nodes) {
		for (SemanticNode node : nodes) {
			if (node.isResourceNode() && matches(node.asResource())) {
				return true;
			}
		}
		return false;
	}

	public abstract boolean matches(ResourceNode resource);

}
