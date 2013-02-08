package org.arastreju.bindings.memory.index;

import de.lichtflut.infra.exceptions.NotYetImplementedException;
import org.arastreju.sge.index.IndexSearcher;
import org.arastreju.sge.naming.QualifiedName;

/**
 * <p>
 *  Temporary mock until lucene implementation is available.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class MockIndex implements IndexSearcher {

    @Override
    public Iterable<QualifiedName> search(String query) {
        throw new NotYetImplementedException();
    }

}
