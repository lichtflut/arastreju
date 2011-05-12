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
package org.arastreju.sge.model.associations;


import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 	AssociationKeeper for a detached Resource.
 * </p>
 * 
 * <p>
 * 	Created: 26.01.2008
 * </p> 
 *
 * @author Oliver Tigges
 */
public class DetachedAssociationKeeper extends AbstractAssociationKeeper implements Serializable {

	/**
	 * Default constructor.
	 */
	public DetachedAssociationKeeper(){
		super();
		markResolved();
	}
	
	public DetachedAssociationKeeper(final Set<Association> associations){
		super(associations);
	}

	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.associations.AssociationKeeper#isAttached()
	 */
	public boolean isAttached() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.arastreju.sge.model.associations.AbstractAssociationKeeper#resolveAssociations()
	 */
	@Override
	protected void resolveAssociations() {
		throw new UnsupportedOperationException("DetachedAssociationKeeper cannot resolve Associations");
	}

}
