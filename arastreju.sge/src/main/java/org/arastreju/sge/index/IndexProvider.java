package org.arastreju.sge.index;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.SimpleResourceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  Provider for indexes for a given context.
 * </p>
 *
 * <p>
 *  Created 12.02.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class IndexProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexProvider.class);

    private static final Context nullCtxDummy = new SimpleContextID(new SimpleResourceID().getQualifiedName());

    /* stores one index per context */
    private final Map<Context, LuceneIndex> indexMap = new HashMap<Context, LuceneIndex>();

    /* placeholder - to be turned into a proper configuration setting */
    private final String indexRoot;

    // ----------------------------------------------------

    public IndexProvider(String indexRootDir) {
        this.indexRoot = indexRootDir;
    }

    // ----------------------------------------------------

    /**
     * Obtain the index for this context.
     * @param context The context.
     * @return The corresponding index.
     */
    public LuceneIndex forContext(Context context) {
        if (context == null)
            context = nullCtxDummy;
        LuceneIndex index;
        synchronized (indexMap) {
            if ((index = indexMap.get(context)) == null) {
                try {
                    indexMap.put(context, (index = new LuceneIndex(indexRoot, context)));
                } catch (IOException e) {
                    String msg = "caught IOException while creating index for context " + context.toURI();
                    LOGGER.error(msg, e);
                    throw new RuntimeException(msg, e);
                }
            }
        }

        return index;
    }

    /**
     * Release the index for this context.
     * @param context The context.
     */
    public void release(Context context) {
        synchronized (indexMap) {
            indexMap.remove(context == null ? nullCtxDummy : context);
        }
    }

}
