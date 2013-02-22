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

    @Override
	public ElementaryDataType getDataType() {
		return value.getDataType();
	}
    @Override
	public Object getValue() {
		return value.getValue();
	}
	
	@Override
	public Locale getLocale() {
		return value.getLocale();
	}
	
	// -----------------------------------------------------

    @Override
	public boolean isResourceNode() {
		return false;
	}

    @Override
	public boolean isValueNode() {
		return true;
	}

    @Override
	public BigDecimal getDecimalValue() {
		return value.getDecimalValue();
	}

    @Override
	public BigInteger getIntegerValue() {
		return value.getIntegerValue();
	}

    @Override
	public String getStringValue() {
		return value.getStringValue();
	}

    @Override
	public Date getTimeValue() {
		return value.getTimeValue();
	}

    @Override
	public Boolean getBooleanValue() {
		return value.getBooleanValue();
	}
	
	// -----------------------------------------------------

    @Override
	public SNScalar asScalar() {
		return value.asScalar();
	}

    @Override
	public SNText asText() {
		return value.asText();
	}

    @Override
	public SNTimeSpec asTimeSpec() {
		return value.asTimeSpec();
	}

    @Override
	public ResourceNode asResource() {
		throw new IllegalStateException("Cannot convert a value to a resource node");
	}

    @Override
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

    @Override
	public int compareTo(ValueNode other) {
		return value.compareTo(other);
	}
	
}
