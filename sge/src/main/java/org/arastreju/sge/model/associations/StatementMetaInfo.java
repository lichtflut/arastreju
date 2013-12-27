package org.arastreju.sge.model.associations;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.StatementOrigin;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *  Meta information about a statement.
 * </p>
 *
 * <p>
 * 	Created Dec 27, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public interface StatementMetaInfo extends Serializable, Cloneable {

    Context[] getContexts();

    Date getTimestamp();

    StatementOrigin getOrigin();

    Date getValidFrom();

    Date getValidUntil();

    //-----------------------------------------------------

    StatementMetaInfo infer();

    StatementMetaInfo inherit();
}
