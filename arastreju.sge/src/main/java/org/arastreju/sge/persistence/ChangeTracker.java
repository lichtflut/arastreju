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
package org.arastreju.sge.persistence;

import java.util.ArrayList;
import java.util.List;

import org.arastreju.sge.model.Statement;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Sep 1, 2010
 * </p>
 *
 * @author Oliver Tigges
 */
public class ChangeTracker {

	@SuppressWarnings("unused")
	private final List<Statement> addedStatements = new ArrayList<Statement>();
	
	@SuppressWarnings("unused")
	private final List<Statement> removedStatements = new ArrayList<Statement>();
}
