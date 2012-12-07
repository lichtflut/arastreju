/*
 * Copyright (C) 2012 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
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
package org.arastreju.sge.context;

import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.nodes.views.SNContext;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Simple identifier of a Context.
 * </p>
 *
 * <p>
 * 	Created Jun 16, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class SimpleContextID extends SimpleResourceID implements Context {

	/**
     * Create a context with given qualified name.
	 * @param qn The qualified name of the context.
	 */
	public SimpleContextID(QualifiedName qn) {
		super(qn);
	}

	/**
     * Create a context with given qualified name.
	 * @param nsUri T.he namespace part.
	 * @param name The name part
	 */
	public SimpleContextID(String nsUri, String name) {
		super(nsUri, name);
	}

	// -----------------------------------------------------
	
	@Override
	public SNContext asResource() {
		return new SNContext(super.asResource());
	}

	public int compareTo(final Context other) {
		return getQualifiedName().compareTo(other.getQualifiedName());
	}

}
