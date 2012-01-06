/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.inferencing;

import java.util.Set;

import org.arastreju.sge.model.Statement;

/**
 * <p>
 *  Base interface for inferencers.
 * </p>
 *
 * <p>
 * 	Created Sep 2, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public interface Inferencer {

	void addInferenced(Statement stmt, Set<Statement> target);

}