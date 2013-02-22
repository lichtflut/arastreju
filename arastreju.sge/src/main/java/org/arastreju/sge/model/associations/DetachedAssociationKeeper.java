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
package org.arastreju.sge.model.associations;


import org.arastreju.sge.eh.ArastrejuRuntimeException;
import org.arastreju.sge.eh.ErrorCodes;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.spi.WorkingContext;

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
public class DetachedAssociationKeeper extends AbstractAssociationKeeper {
	
	/**
	 * Default constructor.
	 */
	public DetachedAssociationKeeper(){
		super();
		markResolved();
	}
	
	public DetachedAssociationKeeper(final Set<Statement> associations){
		super(associations);
		markResolved();
	}
	
	// -----------------------------------------------------
	
	@Override
	public boolean isAttached() {
		return false;
	}

    @Override
    public WorkingContext getConversationContext() {
        // always return null, as there will never be a conversation for detached nodes.
        return null;
    }

    @Override
	protected void resolveAssociations() {
		throw new ArastrejuRuntimeException(ErrorCodes.GENERAL_CONSISTENCY_FAILURE, 
				"DetachedAssociationKeeper cannot resolve Associations: " + this);
	}
	
}
