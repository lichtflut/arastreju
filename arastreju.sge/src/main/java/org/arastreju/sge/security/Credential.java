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
package org.arastreju.sge.security;

import org.arastreju.sge.model.nodes.SemanticNode;

/**
 * <p>
 *  Representation of a credential
 * </p>
 *
 * <p>
 * 	Created Jan 20, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Credential {

	/**
	 * Check if the credential applies to this credential node.
	 * @param node The node representing an credential. 
	 * @return true if this credential applies to the node.
	 */
	boolean applies(SemanticNode node);
	
	/**
	 * Get the string representation of the credential.
	 * @return The string representation.
	 */
	String stringRepesentation();
	
	/**
	 * Check if this credential is empty, i.e. there is no credential.
	 * @return true if the credential is empty.
	 */
	boolean isEmpty();
	
}
