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

import de.lichtflut.infra.exceptions.NotYetSupportedException;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.context.Accessibility;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;

import static org.arastreju.sge.SNOPS.assure;

/**
 * <p>
 * 	Resource representing a context for semantic statements.
 * </p>
 * 
 * <p>
 * 	Created: Feb 22, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class SNContext extends ResourceView implements Context {

    public static SNContext from(SemanticNode node) {
        if (node instanceof SNContext) {
            return (SNContext) node;
        } else if (node instanceof ResourceNode) {
            return new SNContext((ResourceNode) node);
        } else if (node instanceof ResourceID) {
            return new SNContext(node.asResource());
        } else {
            return null;
        }
    }

    // ----------------------------------------------------

    /**
     * Create a new context with given qualified name.
     * @param qn The qualified name of the context.
     */
    public SNContext(QualifiedName qn) {
        super(qn);
        setValue(RDF.TYPE, Aras.CONTEXT);
    }

	/**
	 * Create a new context view for given resource.
	 * @param resource The context resource to be wrapped.
	 */
	public SNContext(ResourceNode resource) {
		super(resource);
	}
	
	// ----------------------------------------------------

    public Accessibility getVisibility() {
        return get(Aras.HAS_VISIBILITY);
    }

    public void setVisibility(Accessibility accessibility) {
        set(Aras.HAS_VISIBILITY, accessibility);
    }

    public Accessibility getAccessibility() {
        return get(Aras.HAS_ACCESSIBILITY);
    }

    public void setAccessibility(Accessibility accessibility) {
        set(Aras.HAS_ACCESSIBILITY, accessibility);
    }

    // ----------------------------------------------------

    public Context getAccessContext() {
        ResourceID ac = resourceValue(Aras.HAS_ACCESS_CONTEXT);
        if (ac != null) {
            return SNContext.from(ac);
        } else {
            return this;
        }
    }

    public void setAccessContext(Context accessContext) {
        setValue(Aras.HAS_ACCESS_CONTEXT, accessContext);
    }

    // ----------------------------------------------------
	
	@Override
	public int compareTo(final Context other) {
		return getQualifiedName().compareTo(other.getQualifiedName());
	}

    // ----------------------------------------------------

    private Accessibility get(ResourceID predicate) {
        ResourceID visibility = resourceValue(predicate);
        if (Aras.PRIVATE.equals(visibility)) {
            return Accessibility.PRIVATE;
        } else if (Aras.PROTECTED.equals(visibility)) {
            return Accessibility.PROTECTED;
        } else if (Aras.PUBLIC.equals(visibility)) {
            return Accessibility.PUBLIC;
        } else {
            return Accessibility.UNKNOWN;
        }
    }

    private void set(ResourceID predicate, Accessibility accessibility) {
        switch (accessibility) {
            case PRIVATE:
                setValue(predicate, Aras.PRIVATE);
                break;
            case PROTECTED:
                setValue(predicate, Aras.PROTECTED);
                break;
            case PUBLIC:
                setValue(predicate, Aras.PUBLIC);
                break;
            case UNKNOWN:
                setValue(predicate, Aras.UNKOWN);
                break;
            default:
                throw new NotYetSupportedException(accessibility);
        }
    }

}
