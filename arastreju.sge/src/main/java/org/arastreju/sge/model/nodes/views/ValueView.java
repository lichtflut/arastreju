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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;

import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.model.nodes.ValueNode;

/**
 * <p>
 *  View for value nodes.
 * </p>
 *
 * <p>
 * 	Created Jan 5, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class ValueView implements ValueNode, Serializable {
	
	private final SNValue value;
	
	// -----------------------------------------------------
	
	/**
	 * Create a new view for given value.
	 */
	public ValueView(final SNValue value) {
		this.value = value;
	}
	
	/**
	 * Create a view for a new value.
	 * @param datatype The datatype.
	 * @param value The value.
	 */
	protected ValueView(final ElementaryDataType datatype, final Object value){
		this.value = new SNValue(datatype, value);
	}
	
	/**
	 * Create a view for a new value.
	 * @param datatype The datatype.
	 * @param value The value.
	 * @param locale The locale.
	 */
	public ValueView(ElementaryDataType datatype, String value, Locale locale) {
		this.value = new SNValue(datatype, value, locale);
	}
	
	// ----------------------------------------------------
	
	/** 
	* {@inheritDoc}
	*/
	public ElementaryDataType getDataType() {
		return value.getDataType();
	}

	/** 
	* {@inheritDoc}
	*/
	public Object getValue() {
		return value.getValue();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Locale getLocale() {
		return value.getLocale();
	}
	
	// -----------------------------------------------------

	/** 
	* {@inheritDoc}
	*/
	public boolean isResourceNode() {
		return false;
	}

	/** 
	* {@inheritDoc}
	*/
	public boolean isValueNode() {
		return true;
	}
	
	/** 
	* {@inheritDoc}
	*/
	public BigDecimal getDecimalValue() {
		return value.getDecimalValue();
	}
	
	/** 
	* {@inheritDoc}
	*/
	public BigInteger getIntegerValue() {
		return value.getIntegerValue();
	}
	
	/** 
	* {@inheritDoc}
	*/
	public String getStringValue() {
		return value.getStringValue();
	}
	
	/** 
	* {@inheritDoc}
	*/
	public Date getTimeValue() {
		return value.getTimeValue();
	}
	
	/** 
	* {@inheritDoc}
	*/
	public Boolean getBooleanValue() {
		return value.getBooleanValue();
	}
	
	// -----------------------------------------------------

	/** 
	* {@inheritDoc}
	*/
	public SNScalar asScalar() {
		return value.asScalar();
	}

	/** 
	* {@inheritDoc}
	*/
	public SNText asText() {
		return value.asText();
	}

	/** 
	* {@inheritDoc}
	*/
	public SNTimeSpec asTimeSpec() {
		return value.asTimeSpec();
	}

	/** 
	* {@inheritDoc}
	*/
	public ResourceNode asResource() {
		throw new IllegalStateException("Cannot convert a value to a resource node");
	}

	/** 
	* {@inheritDoc}
	*/
	public ValueNode asValue() {
		return this;
	}
	
	// -----------------------------------------------------
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return value.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public int compareTo(ValueNode other) {
		return value.compareTo(other);
	}
	
}
