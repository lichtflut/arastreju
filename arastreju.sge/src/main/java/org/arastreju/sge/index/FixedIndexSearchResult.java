package org.arastreju.sge.index;

import org.arastreju.sge.naming.QualifiedName;

import java.util.Iterator;
import java.util.List;

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
public class FixedIndexSearchResult implements IndexSearchResult {

    private final List<QualifiedName> result;

    // ----------------------------------------------------

    public FixedIndexSearchResult(List<QualifiedName> result) {
        this.result = result;
    }

    // ----------------------------------------------------

    /**
     * Close the result object.
     * Should always be called to free resources.
     */
    public void close() {
        // do nothing.
    }

    /**
     * Returns the size of the result entries or -1 if the size is unknown.
     * @return The size.
     */
    public int size() {
        return result.size();
    }

    @Override
    public Iterator<QualifiedName> iterator() {
        return result.iterator();
    }

}
