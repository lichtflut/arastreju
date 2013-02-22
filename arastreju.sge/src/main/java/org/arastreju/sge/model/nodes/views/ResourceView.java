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
import java.util.Date;
import java.util.Set;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.ValueNode;
import org.arastreju.sge.naming.QualifiedName;

import static org.arastreju.sge.SNOPS.associations;

/**
 * <p>
 *	Abstract base for all classes that provide a views on a {@link SNResource}.
 * </p>
 * 
 * <p>
 * 	Created Nov 29, 2009
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class ResourceView implements ResourceNode, Serializable {

	private final ResourceNode resource;
	
	// ------------------------------------------------------
	
	/**
	 * Creates a new views to the given resource.
	 * @param resource The resource to be wrapped.
	 */
	public ResourceView(final ResourceNode resource) {
		if (resource == null) {
			throw new IllegalArgumentException("null resources not allowed!");
		}
		this.resource = resource;
	}
	
	/**
	 * Creates a views to a resource to be created implicitly.
	 */
	protected ResourceView() {
		this.resource = new SNResource();
	}
	
	/**
	 * Creates a views to a resource to be created implicitly.
	 */
	protected ResourceView(QualifiedName qn) {
		this.resource = new SNResource(qn);
	}
	
	// -----------------------------------------------------

    @Override
	public QualifiedName getQualifiedName() {
		return resource.getQualifiedName();
	}

    @Override
	public String toURI() {
		return resource.toURI();
	}

    @Override
	public boolean isBlankNode() {
		return resource.isBlankNode();
	}
	
	// -----------------------------------------------------

    @Override
	public Set<Statement> getAssociations() {
		return resource.getAssociations();
	}

    @Override
	public Statement addAssociation(ResourceID predicate, SemanticNode object, Context... ctx) {
		return resource.addAssociation(predicate, object, ctx);
	}

    @Override
	public boolean removeAssociation(final Statement stmt) {
		return resource.removeAssociation(stmt);
	}
	
	// ------------------------------------------------------

    @Override
	public boolean isValueNode() {
		return false;
	}

    @Override
	public boolean isResourceNode() {
		return true;
	}

    @Override
	public boolean isAttached() {
		return resource.isAttached();
	}

	// -----------------------------------------------------

    @Override
	public ResourceNode asResource() {
		return resource;
	}

    @Override
	public ValueNode asValue() {
		throw new IllegalStateException("Cannot convert a resource to a value node");
	}
	
	// -----------------------------------------------------

    protected Set<Statement> getAssociations(final ResourceID predicate) {
        return associations(resource, predicate);
    }

	protected String stringValue(ResourceID attribute) {
		return SNOPS.string(SNOPS.fetchObject(this, attribute));
	}

    protected ResourceID resourceValue(ResourceID attribute) {
        final SemanticNode node = SNOPS.fetchObject(this, attribute);
        if (node != null && node.isResourceNode()) {
            return node.asResource();
        } else {
            return null;
        }
    }

    protected Boolean booleanValue(ResourceID attribute) {
        final SemanticNode value = SNOPS.fetchObject(this, attribute);
        if (value != null && value.isValueNode()) {
            return value.asValue().getBooleanValue();
        } else {
            return null;
        }
    }
	
	protected Integer intValue(ResourceID attribute) {
		final SemanticNode value = SNOPS.fetchObject(this, attribute);
		if (value != null && value.isValueNode()) {
			return value.asValue().getIntegerValue().intValue();
		} else {
			return null;
		}
	}

    protected Date timeValue(ResourceID attribute) {
        final SemanticNode value = SNOPS.fetchObject(this, attribute);
        if (value != null && value.isValueNode()) {
            return value.asValue().getTimeValue();
        } else {
            return null;
        }
    }
	
	protected void setValue(ResourceID attribute, Object value) {
        if (value != null) {
		    SNOPS.assure(this, attribute, value);
        } else {
            removeValues(attribute);
        }
	}

    protected void removeValues(ResourceID attribute) {
        SNOPS.remove(this, attribute);
    }
	
	// -----------------------------------------------------
	
	@Override
	public String toString() {
		return resource.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return resource.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return resource.hashCode();
	}

}
