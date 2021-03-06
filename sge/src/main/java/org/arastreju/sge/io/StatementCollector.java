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

import org.arastreju.sge.model.DefaultSemanticGraph;
import org.arastreju.sge.model.SemanticGraph;
import org.arastreju.sge.model.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Collector for read statments.
 * </p>
 *
 * <p>
 * 	Created Jan 14, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class StatementCollector implements ReadStatementListener {
	
	private final List<Statement> statements = new ArrayList<Statement>();

	// -----------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public void onNewStatement(final Statement stmt) {
		statements.add(stmt);
	}
	
	// ----------------------------------------------------
	
	/**
	 * @return the collected Associations.
	 */
	public List<Statement> getStatements() {
		return statements;
	}

	/**
	 * @return
	 */
	public SemanticGraph toSemanticGraph() {
		return new DefaultSemanticGraph(statements);
	}

}
