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
package org.arastreju.sge.model.nodes;


/**
 * <p>
 * 	Base interface of all resources and values in a semantic model.
 * </p>
 * 
 * <p>
 * 	Created: 09.11.2009
 * </p>
 *
 * @author Oliver Tigges 
 */
public interface SemanticNode {

	/**
	 * States if this node is attached to a managed semantic model.
	 * @return true if the node is attached.
	 */
	boolean isAttached();
	
	/**
	 * States if this node is a value node.
	 * @return true if this node is a value node.
	 */
	boolean isValueNode();

	/**
	 * States if this node is a resource node.
	 * @return true if this node is a resource node.
	 */
	boolean isResourceNode();

	/**
	 * Converts this node into a resource.
	 * An {@link UnsupportedOperationException} will be thrown if <code>isResourceNode</code> returns false; 
	 * @return The corresponding resource.
	 */
	 ResourceNode asResource();
	
	/**
	 * Converts this node into a value.
	 * An {@link UnsupportedOperationException} will be thrown if <code>isValueNode</code> returns false; 
	 * @return The corresponding value.
	 */
	 ValueNode asValue();
	
}