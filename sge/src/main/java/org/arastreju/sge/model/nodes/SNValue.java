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
package org.arastreju.sge.model.nodes;

import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.Infra;
import org.arastreju.sge.model.nodes.views.SNScalar;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.model.nodes.views.SNTimeSpec;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * 	Base for data nodes. Data nodes may have no outgoing associations
 *  but may only be associated from resource nodes.
 * </p>
 * 
 * <p>
 * 	Created: 16.01.2009
 * </p>
 *
 * @author Oliver Tigges
 */
public class SNValue implements ValueNode, Serializable {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	private final ElementaryDataType datatype;

	private final Object value;

	private final Locale locale;

	// -----------------------------------------------------

	/**
	 * Constructor.
	 * @param datatype The datatype.
	 * @param value The value.
	 */
	public SNValue(final ElementaryDataType datatype, final Object value) {
		this(datatype, value, null);
	}

	/**
	 * Constructor.
	 * @param datatype The datatype.
	 * @param value The value.
	 * @param locale The locale
	 */
	public SNValue(final ElementaryDataType datatype, final Object value, final Locale locale) {
		if (value == null) {
			throw new IllegalArgumentException("Value may not be null");
		}
		this.datatype = datatype;
		this.locale = locale;
		try {
			this.value = convert(value, datatype);
		} catch (ParseException e) {
			throw new IllegalStateException("Value not of expected type", e);
		}
	}

	//-----------------------------------------------------

	public Object getValue(){
		switch (datatype) {
		case BOOLEAN:
			return getBooleanValue();
		case URI:
		case STRING:
			return getStringValue();
		case DECIMAL:
			return getDecimalValue();
		case INTEGER:
			return getIntegerValue();
		case DATE:
		case TIME_OF_DAY:
		case TIMESTAMP:
			return getTimeValue();
		default:
			throw new IllegalStateException("Cannot determine type of value: " + value + " (" + datatype + ")");
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public ElementaryDataType getDataType() {
		return datatype;
	}

	// ----------------------------------------------------

	public boolean isResourceNode() {
		return false;
	}

	public boolean isValueNode() {
		return true;
	}

	public ResourceNode asResource() {
		throw new IllegalStateException("Not a resource: " + this);
	}

	public ValueNode asValue() {
		return this;
	}

	// ------------------------------------------------------

	public String getStringValue() {
		if (value == null){
			return "";
		}
		switch (datatype) {
		case DATE:
		case TIME_OF_DAY:
		case TIMESTAMP:
			return DATE_FORMAT.format(getTimeValue());
		case BOOLEAN:
			return value.toString();
		default:
			return value.toString();
		}
	}

	public BigDecimal getDecimalValue() {
		if (value instanceof String){
			return new BigDecimal((String) value);
		}
		return (BigDecimal) value;
	}

	public BigInteger getIntegerValue() {
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
		if (value instanceof String){
			return new BigInteger((String) value);
		}
        if (value instanceof Number) {
            Number number = (Number) value;
            return BigInteger.valueOf(number.longValue());
        }
        throw new IllegalStateException("Cannot convert '" + value + "' to an BigInteger.");
	}

	public Date getTimeValue() {
		return (Date) value;
	}

	public Boolean getBooleanValue() {
		return (Boolean) value;
	}

	// -----------------------------------------------------

	public SNTimeSpec asTimeSpec() {
		return new SNTimeSpec(this);
	}

	public SNScalar asScalar() {
		return new SNScalar(this);
	}

	public SNText asText() {
		return new SNText(this);
	}

	// -----------------------------------------------------

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getStringValue());
		if (locale != null) {
			sb.append(" [").append(locale).append("]");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((datatype == null) ? 0 : datatype.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ValueNode) {
			ValueNode other = (ValueNode) obj;
			if (!Infra.equals(value, other.getValue())) {
				return false;
			}
			if (!Infra.equals(datatype, other.getDataType())) {
				return false;
			}
			if (!Infra.equals(locale, other.getLocale())) {
				return false;
			}
			return true;
		}
		return super.equals(obj);
	}

	// -----------------------------------------------------

	public int compareTo(final ValueNode other) {
		switch (datatype) {
		case BOOLEAN:
			return getBooleanValue().compareTo(other.getBooleanValue());
		case URI:
		case STRING:
			return getStringValue().compareTo(other.getStringValue());
		case DECIMAL:
			return getDecimalValue().compareTo(other.getDecimalValue());
		case INTEGER:
			return getIntegerValue().compareTo(other.getIntegerValue());
		case DATE:
		case TIME_OF_DAY:
		case TIMESTAMP:
			return getTimeValue().compareTo(other.getTimeValue());
		default:
			throw new IllegalStateException("Cannot determine type of value: " + value + " (" + datatype + ")");
		}
	}

    // -- CONVERSIONS -------------------------------------

    /**
     * Converts a String value to needed type, if necessary.
     * @throws ParseException
     */
    private Object convert(final Object value, final ElementaryDataType datatype) throws ParseException {
        if (value instanceof String) {
            return convertString((String) value, datatype);
        }
        return value;
    }

    private Object convertString(final String value, final ElementaryDataType datatype) throws ParseException {
        switch (datatype) {
            case DATE:
            case TIME_OF_DAY:
            case TIMESTAMP:
                return DATE_FORMAT.parse(value);
            case BOOLEAN:
                return Boolean.parseBoolean(value);
            case DECIMAL:
                return new BigDecimal(value);
            case INTEGER:
                return new BigInteger(value);
            case URI:
            case STRING:
            case PROPER_NAME:
            case TERM:
            case UNDEFINED:
                return value;

            case RESOURCE:
                throw new IllegalStateException("SNValue may not contain a value of type RESOURCE.");

            default:
                throw new IllegalStateException("Unsupported datatype: " + datatype);
        }
    }

}