package org.arastreju.sge.index;

import org.arastreju.sge.naming.QualifiedName;

import java.util.Iterator;

/**
 * <p>
 *  Searcher for the Arastreju index.
 * </p>
 *
 * <p>
 *  Created 01.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface IndexSearcher {

    /**
     * Search the index.
     * @param query The lucene query.
     * @return An iterator over the search results.
     */
    Iterator<QualifiedName> search(String query);
}
