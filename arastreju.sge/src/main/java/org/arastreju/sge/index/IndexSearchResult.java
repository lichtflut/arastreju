package org.arastreju.sge.index;

import org.apache.lucene.search.TopDocs;
import org.arastreju.sge.naming.QualifiedName;

import java.util.Iterator;

/**
 * <p>
 *  Represents the result of a search using IndexSearcher.
 * </p>
 *
 * <p>
 *  Created 12.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public interface IndexSearchResult extends Iterable<QualifiedName> {

    /**
     * Close the result object.
     * Should always be called to free resources.
     */
    void close();

    /**
     * Returns the size of the result entries or -1 if the size is unknown.
     * @return The size.
     */
    int size();

}
