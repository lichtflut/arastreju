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
package org.arastreju.sge.model.nodes.views;

import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.SNValue;

/**
 * Representation of an external URI.
 * This node has it't own technical URI like all other nodes.
 * Additionally it references an external URI.
 * 
 * Created: 11.08.2009
 *
 * @author Oliver Tigges 
 */
public class SNUri extends ValueView {

	/**
	 * Creates a new transient SNUri with given URI as value.
	 * @param uri
	 */
	public SNUri(final String uri){
		super(ElementaryDataType.URI, uri);
	}
	
	/**
	 * Creates a new URI view for given value.
	 * @param value The value to be wrapped.
	 */
	public SNUri(final SNValue value) {
		super(value);
	}
	
	// ------------------------------------------------------
	
	/**
	 * Gets the external URI referenced by this node.
	 */
	public String getReferencedUri(){
		return getStringValue();
	}
	
}
