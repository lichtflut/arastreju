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

import org.arastreju.sge.model.nodes.SNValue;


/**
 * Semantic node representing a name.
 * A name may be complex, i.e. it consists of two ore more subnames.
 * 
 * Created: 19.03.2008
 *
 * @author Oliver Tigges 
 */
public class SNName extends ValueView {
	
	/**
	 * Creates a new name view for given value.
	 * @param value The value to be wrapped.
	 */
	public SNName(final SNValue value) {
		super(value);
	}
	
}
