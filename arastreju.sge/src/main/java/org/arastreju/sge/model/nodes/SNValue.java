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
package org.arastreju.sge.model.nodes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.views.SNName;
import org.arastreju.sge.model.nodes.views.SNScalar;
import org.arastreju.sge.model.nodes.views.SNTerm;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.model.nodes.views.SNTimeSpec;
import org.arastreju.sge.model.nodes.views.SNUri;

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
	
	private final ElementaryDataType datatype;
	
	private final Object value;

	/**
	 * Constructor.
	 * @param datatype The datatype.
	 * @param value The value.
	 */
	public SNValue(final ElementaryDataType datatype, final Object value) {
		this.datatype = datatype;
		this.value = value;
	}
	
	//-----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#isResourceNode()
	 */
	public boolean isResourceNode() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#isValueNode()
	 */
	public boolean isValueNode() {
		return true;
	}
	
	public boolean isAttached() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#getDataType()
	 */
	public ElementaryDataType getDataType() {
		return datatype;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.SemanticNode#asResource()
	 */
	public ResourceNode asResource() {
		throw new IllegalStateException("Not a resource: " + this);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.SemanticNode#asValue()
	 */
	public ValueNode asValue() {
		return this;
	}
	
	// ------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#getValue()
	 */
	public Object getValue(){
		switch (datatype) {
		case BOOLEAN:
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
			return null;
		}
	}
	

	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ValueNode#getStringValue()
	 */
	public String getStringValue() {
		if (value == null){
			return "";
		} 
		return value.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ValueNode#getDecimalValue()
	 */
	public BigDecimal getDecimalValue() {
		if (value instanceof String){
			return new BigDecimal((String) value);
		}
		return (BigDecimal) value;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ValueNode#getIntegerValue()
	 */
	public BigInteger getIntegerValue() {
		if (value instanceof String){
			return new BigInteger((String) value);
		}
		return (BigInteger) value;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ValueNode#getTimeValue()
	 */
	public Date getTimeValue() {
		return (Date) value;
	}
	
	// -----------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#asUri()
	 */
	public SNUri asUri() {
		return new SNUri(this);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#asTimeSpec()
	 */
	public SNTimeSpec asTimeSpec() {
		return new SNTimeSpec(this);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#asScalar()
	 */
	public SNScalar asScalar() {
		return new SNScalar(this);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#asText()
	 */
	public SNText asText() {
		return new SNText(this);
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#asTerm()
	 */
	public SNTerm asTerm(){
		return new SNTerm(this);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#asName()
	 */
	public SNName asName(){
		return new SNName(this);
	}
	
	// -----------------------------------------------------
	
	@Override
	public String toString() {
		return getStringValue();
	}
	
}