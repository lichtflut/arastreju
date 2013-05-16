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
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.context.Accessibility;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;

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

	/**
	 * Create a new context view for given resource.
	 * @param resource The context resource to be wrapped.
	 */
	public SNContext(final ResourceNode resource) {
		super(resource);
	}
	
	// ----------------------------------------------------

    public Accessibility getVisibility() {
        ResourceID visibility = resourceValue(Aras.HAS_VISIBILITY);
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

    public void setVisibility(Accessibility accessibility) {
        switch (accessibility) {
            case PRIVATE:
                setValue(Aras.HAS_VISIBILITY, Aras.PRIVATE);
                break;
            case PROTECTED:
                setValue(Aras.HAS_VISIBILITY, Aras.PROTECTED);
                break;
            case PUBLIC:
                setValue(Aras.HAS_VISIBILITY, Aras.PUBLIC);
                break;
            case UNKNOWN:
                setValue(Aras.HAS_VISIBILITY, Aras.UNKOWN);
                break;
            default:
                throw new NotYetSupportedException(accessibility);
        }
    }

    // ----------------------------------------------------
	
	@Override
	public int compareTo(final Context other) {
		return getQualifiedName().compareTo(other.getQualifiedName());
	}
	
}
