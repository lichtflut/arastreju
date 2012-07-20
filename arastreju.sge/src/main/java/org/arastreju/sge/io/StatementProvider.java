package org.arastreju.sge.io;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.naming.Namespace;

import java.util.Collection;

/**
 * <p>
 *  Provides statements for streaming them out.
 * </p>
 *
 * <p>
 * Created 20.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public interface StatementProvider extends Iterable<Statement> {

    Collection<Namespace> getNamespaces();

}
