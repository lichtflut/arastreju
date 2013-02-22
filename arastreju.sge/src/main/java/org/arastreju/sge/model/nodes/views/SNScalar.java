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
/**
 * 
 */
package org.arastreju.sge.model.nodes.views;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.SNValue;

/**
 * Representation of a scalar value, i.e. a number without any direction or unit.
 * 
 * Created: 16.01.2009
 *
 * @author Oliver Tigges
 */
public class SNScalar extends ValueView {

	/**
	 * Creates a new scalar integer value.
	 * @param integerValue
	 */
	public SNScalar(final BigInteger integerValue){
		super(ElementaryDataType.INTEGER, integerValue);
	}
	
	/**
	 * Creates a new scalar integer value.
	 * @param integerValue The integer value.
	 */
	public SNScalar(final Integer integerValue){
		this(BigInteger.valueOf(integerValue));
	}
	
	/**
	 * Creates a new scalar decimal value.
	 * @param decimalValue The decimal value.
	 */
	public SNScalar(final BigDecimal decimalValue){
		super(ElementaryDataType.DECIMAL, decimalValue);
	}
	
	/**
	 * Creates a new scalar view for given value.
	 * @param value The value to be wrapped.
	 */
	public SNScalar(final SNValue value) {
		super(value);
	}
	
}
