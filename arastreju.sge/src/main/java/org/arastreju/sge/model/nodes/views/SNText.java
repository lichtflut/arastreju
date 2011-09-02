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
package org.arastreju.sge.model.nodes.views;

import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.SNValue;

/**
 * <p>
 * 	Representation of a text value.
 * </p>
 * 
 * Created: 25.05.2009
 *
 * @author Oliver Tigges
 */
public class SNText extends ValueView {

	/**
	 * Creates a new text node.
	 * @param text
	 */
	public SNText(final String text){
		super(ElementaryDataType.STRING, text);
	}
	
	/**
	 * Creates a new text view for given value.
	 * @param value The value to be wrapped.
	 */
	public SNText(final SNValue value) {
		super(value);
	}
	
}
