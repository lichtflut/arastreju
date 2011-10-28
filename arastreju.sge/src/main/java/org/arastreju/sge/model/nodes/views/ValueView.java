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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

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
	
	protected ValueView(final ElementaryDataType datatype, final Object value){
		this.value = new SNValue(datatype, value);
	}
	
	// -----------------------------------------------------
	

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.INode#isAttached()
	 */
	public boolean isAttached() {
		return value.isAttached();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.INode#isResourceNode()
	 */
	public boolean isResourceNode() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.INode#isValueNode()
	 */
	public boolean isValueNode() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ValueNode#getDecimalValue()
	 */
	public BigDecimal getDecimalValue() {
		return value.getDecimalValue();
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ValueNode#getIntegerValue()
	 */
	public BigInteger getIntegerValue() {
		return value.getIntegerValue();
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ValueNode#getStringValue()
	 */
	public String getStringValue() {
		return value.getStringValue();
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.nodes.ValueNode#getTimeValue()
	 */
	public Date getTimeValue() {
		return value.getTimeValue();
	}
	
	// -----------------------------------------------------

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#asScalar()
	 */
	public SNScalar asScalar() {
		return value.asScalar();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#asText()
	 */
	public SNText asText() {
		return value.asText();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#asTimeSpec()
	 */
	public SNTimeSpec asTimeSpec() {
		return value.asTimeSpec();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#asUri()
	 */
	public SNUri asUri() {
		return value.asUri();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#getDataType()
	 */
	public ElementaryDataType getDataType() {
		return value.getDataType();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.sn.ValueNode#getValue()
	 */
	public Object getValue() {
		return value.getValue();
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#asResource()
	 */
	public ResourceNode asResource() {
		throw new IllegalStateException("Cannot convert a value to a resource node");
	}

	/* (non-Javadoc)
	 * @see org.arastreju.api.ontology.model.SemanticNode#asValue()
	 */
	public ValueNode asValue() {
		return value;
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
	
}
