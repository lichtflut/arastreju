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
import org.arastreju.sge.naming.Namespace;
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
	 * @param qn
	 */
	public SimpleContextID(QualifiedName qn) {
		super(qn);
	}

	/**
	 * @param nsUri
	 * @param name
	 */
	public SimpleContextID(String nsUri, String name) {
		super(nsUri, name);
	}

	/**
	 * @param namespace
	 * @param name
	 */
	public SimpleContextID(Namespace namespace, String name) {
		super(namespace, name);
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.SimpleResourceID#asResource()
	 */
	@Override
	public SNContext asResource() {
		return new SNContext(super.asResource());
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final Context other) {
		return getQualifiedName().compareTo(other.getQualifiedName());
	}

}
