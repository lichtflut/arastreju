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
package org.arastreju.sge.io;

import org.arastreju.sge.Conversation;
import org.arastreju.sge.model.Statement;

/**
 * <p>
 *  Listener that stores the statements directly.
 * </p>
 *
 * <p>
 * 	Created Feb 25, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class StatementStorer implements ReadStatementListener {
	
	private final Conversation conversation;
	
	// ----------------------------------------------------

	/**
	 * @param conversation The modelling conversation.
	 */
	public StatementStorer(Conversation conversation) {
		this.conversation = conversation;
	}

	// ----------------------------------------------------

	@Override
	public void onNewStatement(Statement stmt) {
		conversation.addStatement(stmt);
	}

}
